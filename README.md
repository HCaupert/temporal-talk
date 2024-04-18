# La fin des appli event-based ? Orchestration avec Temporal

## Important
Selon les itérations du live coding, certaines petites variations (et bugs 😛) 
peuvent se glisser. Pensez à checkout sur la branche du talk !

## Running the apps
### Dashboard: 
```bash
cd dashboard && yarn install && yarn dev
```

### Temporal: 
Pour lancer le cluster Temporal : 
```bash
docker compose up
```

Puis ajouter le search attribute `ShippingStatus`:
```bash
tctl admin cluster add-search-attributes --name ShippingStatus --type Keyword
```

### Backend: 

Même si d'un point de vue Temporal, je suis sur une archi distribuée, 
pour les besoins de la démo je lance tout comme un monolithe. 

Le runnable se trouve dans `/gateway`

Pour lancer: 
```bash
cd gateway && mvn clean install && mvn springboot:run
```



## Disclaimer 

### Dashboard 
Le front est quick and dirty

### Workflow
En terme de design, le workflow developpé ne respecte pas forcément les meilleures pratiques.

On pourrait facilement se retrouver dans une situation de soft-lock, par exemple si un des signal arrive a un moment non attendu.
Pour fixer ça il faudrait par exemple : 
- Rajouter de la validation quand on reçoit un signal, pour être sur que notre commande ne revienne pas dans un état précédent
- Implémenter des signaux idempotents. Par exemple avec des triggers boolean : `orderIsShipped: Boolean`. 
De cette manière rejouer le signal n'aura pas d'impact. 
