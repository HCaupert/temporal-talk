## Setup
- ReLancer le cluster temporal (vider + search attribute)
- Lancer le dashboard
- Onglet à ouvrir :
  - `http://localhost:3000`
  - `http://localhost:8080/api/swagger-ui/index.html`
  - `http://localhost:8081`
- IJ:
  - ProcessOrderWorkflowImpl

## First Flow

### Préparer le flow
- `ProcessOrderWorkflowImpl`
- Implémenter le flow
- Bon il faut injecter le shipping service
- Temporal a un sdk
- Implémenter 
- Il nous faut des options ! 
  - > acop
- Bon pour pouvoir lancer une activité depuis un workflow, il faut faire des trucs à l'interface
- `ShippingService`
  - `@ActivityInterface`
- On fait pareil pour le inventoryService

### Préparer le controller
- On a `WorkflowClient` qui nous permet de se reconnecter au contexte temporal
- C'est un objet qui vient du sdk
```kotlin
val workflow = workflowClient.newWorkflowStub<ProcessOrderWorkflow>(options)
```
- Il nous faut des options : 
  - > woop
- Puis finalement on peut lancer
  - > wcs
```kotlin
WorkflowClient.start(workflow::processOrder, order)
```
- Pour faire ça il faut pareil annoter l'interface : 
  - `@WorkflowInterface`
  - `@WorkflowMethod`
- Bon bah go ?
- Lancer l'app
- Lancer le end-point swagger

#### ------------ Pause respirer ------------
- Bon bah il ne se passe rien...
- Heureusement on a le dashboard ! 
- Regardons un peu ce qu'on voit
- Bah il nous dit que y'a pas de worker...

### Les workers
- `OrderWorker`
```kotlin
val worker = workerFactory.newWorker(MyTemporalQueue.ORDER.name)
    .registerWorkflowImplementationTypes(ProcessOrderWorkflowImpl::class.java)
```
- Ici, c'est important de mentionner qu'on donne une classe 
- Le cycle de vie est géré par temporal

`ShippingWorker`
```kotlin
val worker = workerFactory.newWorker(MyTemporalQueue.SHIPPING.name)
    .registerActivitiesImplementations(shippingService)
```
- Ici, on peut donner une instance de notre choix
- Je pourrais créer une instance de pojo
- Je donne un bean spring

- On relance ?
- Bravo !!

- #### EAU + SLIDES

## Visibility 1
### List
- Le dashboard est super chouette, mais pas super pratique pour monitorer mes commandes
- Il y a plein de filtres qui ont l'air intéressants
- Bah ça tombe bien y'a une api pour ça
- `OrderController`
```kotlin
return workflowClient.listExecutions("")
```
- > mawf
- Relancer le back
- Montrer _MyShippingLogistics_
- Bon bah bravo, on sait lister nos process
- On verra plus tard pour peaufiner un peu ça

### Info
- `OrderController.getOrder`
- On aimerait bien implémenter le "get order info" 
- `OrderInfo`
- On aimerait avoir : 
  - Des infos sur ma commande (input)
  - Des infos sur l'état actuel (shipping, payment)
- `OrderController.getOrder`
- En fait nous, on aimerait faire : 
```kotlin
return workflowClient.newWorkflowStub<ProcessOrderWorkflow>(orderId)
  .getOrder() 
```
- `alt + entr` pour crée la méthode dans l'interface
- Bon bah en fait, on peut le faire, il faut juste rajouter `@QueryMethod`
- Puis ensuite il faut l'implémenter
  - > goi
- On a besoin d'un certain nombre de trucs
- La commande c'est ok
- Pour le reste 
```kotlin

private lateinit var order: Order
private var shippingStatus = ShippingStatus.PENDING
private var paymentStatus = PaymentStatus.AUTHORIZED
private var trackingNumber: String? = null

override fun processOrder(order: Order) {
  this.order = order

  //Shipping
  shippingService.shipOrder(order)
  shippingStatus = ShippingStatus.SHIPPED
}
 
```
- Relancer
- Ca y est on peut voir nos commandes ! 

## Payment
### Await
- Bon en vrai ce serait chouette de gérer le paiement avant d'envoyer
- On veut rajouter un check sur l'authorize 
  - Vous savez c'est quand le paiement est en mode "à venir" sur votre compte
- "On voudrait attendre que le paiement soit autorisé"
- Répéter + `Workflow.await { paymentStatus == PaymentStatus.AUTHORIZED }`
- Bon après pas de magie, il nous faudrait un truc qui vienne l'update
- Il nous faudrait un truc qui fait ça en fait 
```kotlin
fun markPaymentAsAuthorized(){
    paymentStatus = PaymentStatus.AUTHORIZED
}
```
- Ce serait super chouette que ce truc-là soit disponible à travers temporal
- rajouter `override` + `alt + entr`
- Cette fois si on veut pas récupérer/query de l'info
- On veut en envoyer
- On doit donc rajouter `@SignalMethod`
- Ca veut dire que depuis notre `PaymentController`
- Sur le endpoint de mon psp 
  - Un psp c'est un prestataire de paiement, genre paypal, stripe etc...
- Je peux rajouter :
```kotlin
workflowClient.newWorkflowStub<ProcessOrderWorkflow>(orderId.toString())
    .markPaymentAsAuthorized()
```
- Ok on est pas mal -> Petit test dashboard

### Capture payment 
- Bon ce serait pas mal de capturer le paiement quand même
- Il faut injecter le service de paiement
  - > payse
- puis mettre à jour le flow
```kotlin
// Capture payment
paymentService.capturePayment(order.paymentId)
paymentStatus = PaymentStatus.CAPTURED
```
- Montrer les logs
- Bravo !


### Search Attribute
- Je sais pas vous mais moi ça me trigger le status _"en attente du paiement"_
- On fix ? 
- Aller dans le dashboard
- Ce serait bien de pouvoir rajouter une colonne -> Ca s'appelle un search attribute
- Run la commande dans le docker
- Remontrer le dashboard
- `ProcessOrderWorkflowImpl`
- On va override le setter, comme ça à chaque fois que je change la valeur
- Je dis à temporal quelle est la nouvelle valeur.
```kotlin
private var shippingStatus = ShippingStatus.PENDING
  set(value) {
    mySetSearchAttribute(MySearchAttributes.SHIPPING_STATUS, value)
    field = value
  } 
```
- Relancer une commande et montrer le dashboard
- Plus qu'à mettre à jour notre controller pour qu'il lise la valeur
- `OrderController.listOrders`
  - `shippingStatus = ShippingStatus.valueOfOrPending(wf.mySearchAttribute(MySearchAttributes.SHIPPING_STATUS)),`


### Temporal IT 
- Bon jusque là c'était plutôt classique comme dev
- En fait quand on a temporal, on peut un peu changer notre manière de coder pour utiliser au mieux les possibilités
- Target: 
```kotlin
override fun processOrder(order: Order) {
  this.order = order

  //Payment Authorization
  Workflow.await { paymentStatus == PaymentStatus.AUTHORIZED }
  shippingStatus = ShippingStatus.AWAITING_PREPARATION

  // Prepare shipping
  Workflow.await { shippingStatus == ShippingStatus.PREPARED }

  // Capture payment
  paymentService.capturePayment(order.paymentId)
  paymentStatus = PaymentStatus.CAPTURED
  shippingStatus = ShippingStatus.AWAITING_SHIPPING

  // Shipping
  Workflow.await { shippingStatus == ShippingStatus.SHIPPED }
}
```
- Il nous faut deux méthodes pour venir mettre à jour ce status de livraison
  - > shpr
- Et au passage, on a rajouté le numéro de suivi
- On fait pareil dans l'interface `ProcessOrderWorkflow`
  - > sish 
- Puis faut aller remplir le `ShippingController` correctement
- Run une commande en side by side
