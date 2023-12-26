//@ts-ignore
import Reveal from "reveal.js";
//@ts-ignore
import Markdown from "reveal.js/plugin/markdown/markdown.esm.js";
import "reveal.js/dist/reveal.css";
import "reveal.js/dist/theme/black.css";
import "./style.css";

let deck = new Reveal({
  plugins: [Markdown],
});
deck.initialize({
  //@ts-ignore
  plugins: [RevealLoadContent],
});
