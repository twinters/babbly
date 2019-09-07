# Babbly
Babbly is a language designed for efficient and elegant modeling of custom text generators using generative weighted context free grammars.

## Introduction
Babbly is a language for modelling text generators.
It is inspired by context-free grammars and regular expression-syntax, and adds weights to more easily model generators.
It also supports using externally declared text generators to be used within the language, as well as importing other Babbly files or word lists.

## Dependencies

In order for this code to work, you will need to install [Gradle](https://gradle.org/), as well as download the following repositories in the same folder as `babbly`:
- [text-util](https://github.com/twinters/text-util)
- [generator-util](https://github.com/twinters/generator-util)

## Example

Consider the following example: 
```
import firstname.words

food = pasta|pizza|sushi
main = {
  3: <firstname> loves <food>!
  1: <firstname> (quite|reasonably|fairly) likes eating <food>. Oo{1,3}h, I really hope she'll join!
  1: <firstname:protagonist> is not (quite){.5} fond of <food:>. <firstname:protagonist> will thus not go to the <food:> (restaurant|place).
}
```

In this example, a file called `firstname.words` consisting of a new-line separated first names list is loaded in.
This thus loads in a text generator which can be called using `<firstname>`, and will uniformly select any of the names in the list.

It then shows how a simple generator can be declared using syntax similar to regular expressions, but for generative purposes. `food` will thus also uniformly select between the three specified dishes.

The `main` declaration is called by default if no other name is specified for generation.
This variable will 3/5=60% of the time generate that a particular first names really loves a particular type of food, thanks to the weight that has been given to this clause.
The second clause shows that the `|` mark can be used within sentences to quickly bring variation into sentences. The second part of the generator uses the `{x,y}` syntax, which specifies that the previous part or token should be repeated at least `x` and at most `y` times. It will thus generate `Ooh`, `Oooh` or `Ooooh`.
The third clause shows that variables can also be locked within the same generation by using `:` to lock to a record. With this, it makes sure that the generators are not rerolled with different names and food.
The record name can also be of zero length (as done with `<food:>`).
There is also a 50% probability of 'quite' being used in the generation, as the `{.5}` specifies a probability of the previous part or token.


## Examples

More examples can be found in the following locations:
- Babbly's [Example Folder](https://github.com/twinters/babbly/tree/master/examples)
- *(Dutch)* [Samson & Gert Twitterbots](https://twitter.com/thomas_wint/lists/samson-bots) all use Babbly: [SamsonBot](https://github.com/twinters/samson-bot/blob/master/src/main/resources/data/templates/samson.decl), [GertBot](https://github.com/twinters/samson-bot/blob/master/src/main/resources/data/templates/gert-verbeter.decl), [BurgemeesterBot](https://github.com/twinters/burgemeester-bot/blob/master/src/main/resources/templates/toespraak.decl), [AlbertoBot](https://github.com/twinters/alberto-bot/blob/master/src/main/resources/templates/alberto.decl), [OctaafBot](https://github.com/twinters/octaaf-bot/blob/master/src/main/resources/templates/octaaf.decl), [JeannineBot](https://github.com/twinters/jeannine-bot/blob/master/src/main/resources/templates/jeannine.decl).

