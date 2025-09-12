package com.algorabedu.landing.page

import com.algorabedu.landing.Translation
import com.algorabedu.landing.tr
import java.time.Year
import scalatags.Text.TypedTag
import scalatags.Text.all.*
import scalatags.Text.svgAttrs.d
import scalatags.Text.svgAttrs.fill
import scalatags.Text.svgAttrs.rx
import scalatags.Text.svgAttrs.stroke
import scalatags.Text.svgAttrs.viewBox
import scalatags.Text.svgAttrs.x
import scalatags.Text.svgAttrs.y
import scalatags.Text.svgTags.g
import scalatags.Text.svgTags.path
import scalatags.Text.svgTags.rect
import scalatags.Text.svgTags.svg
import scalatags.Text.tags2.aside
import scalatags.Text.tags2.details
import scalatags.Text.tags2.summary
import scalatags.Text.tags.tr as trow
import scalatags.Text.tags2.{title => titleTag}

val headerCode: TypedTag[String] =
  numberedCode(cls := "basis-1/2 w-full")(
    """enum Tree[cov Element]:
      |  case Leaf(value: Element)
      |  case Node(value: Element, children: NonEmptyList[Tree[Element]])
      |
      |-- Root is min
      |def minMax[Element](tree: Element): Tree[Element] = tree match
      |  case Leaf(value)       then value
      |  case Node(_, children) then children.map(maxMin).minimum
      |
      |-- Root is max
      |def maxMin[Element](tree: Element): Tree[Element] = tree match
      |  case Leaf(value)       then value
      |  case Node(_, children) then children.map(minMax).maximum""".stripMargin,
  )

val quickStartCode: TypedTag[String] =
  numberedCode(cls := "tab-content overflow-x-auto")(
    """println("Hello World")
      |val name = ask("What's your name?")
      |println("Hello $name!")""".stripMargin
  )

val quickStartPythonCode: TypedTag[String] =
  numberedCode(cls := "tab-content overflow-x-auto")(
    """print("Hello World")
      |name = input("What's your name?")
      |print(f"Hello {name}!")""".stripMargin
  )

val quickStartCCode: TypedTag[String] =
  numberedCode(cls := "tab-content overflow-x-auto")(
    """printf("Hello World\\n");
      |
      |char word[256];
      |fgets(word, sizeof(word), stdin);
      |
      |printf("Hello %s!\\n");""".stripMargin
  )


val expressivityCode: TypedTag[String] =
  numberedCode(cls := "tab-content overflow-x-auto")(
    """enum LinkedList[A]:
      |  case Empty
      |  case Node(head: A, tail: LinkedList[A])
      |
      |  def append(element: A): LinkedList[A] = this match
      |    case Empty            then Node(head, Empty)
      |    case Node(head, tail) then Node(head, tail.append(element))""".stripMargin
  )

val expressivityPythonCode: TypedTag[String] =
  numberedCode(cls := "tab-content overflow-x-auto")(
    """class LinkedList:
      |    def append(self, element):
      |        pass
      |  
      |class Empty(LinkedList):
      |    def append(self, element):
      |        return Node(element, Empty())
      |        
      |class Node(LinkedList):
      |    
      |    def __init__(self, head, tail):
      |        self.head = head
      |        self.tail = tail
      |    
      |    def append(self, element):
      |        return Node(self.head, self.tail.append(element))""".stripMargin
  )

val expressivityCCode: TypedTag[String] =
  numberedCode(cls := "tab-content overflow-x-auto")(
    """#include <stdlib.h>
      |
      |typedef struct Node {
      |    int head;
      |    struct Node *tail;
      |} LinkedList;
      |
      |LinkedList *createNode(int head, LinkedList *tail) {
      |    LinkedList *result = malloc(sizeof(LinkedList));
      |    result->head = head;
      |    result->tail = tail;
      |    
      |    return result;
      |}
      |
      |LinkedList *append(LinkedList *list, int element) {
      |    if(list == NULL) return createNode(element, list);
      |    else return createNode(list->head, append(list->tail, element));
      |}""".stripMargin
  )

val assitanceCode: TypedTag[String] =
  numberedCode(cls := "overflow-x-auto")(
    """record User(name: String, age: Int)
      |
      |val user = User("John", 20)
      |println("Hello ${user.username} !")""".stripMargin
  )

val professionalisationCode: TypedTag[String] =
  numberedCode(cls := "overflow-x-auto")(
    """---
      |Create a new user.
      |- param  name the user's name. Should not be empty.
      |- param  age the user's age. Should be positive.
      |- return a new User with the given fields.
      |---
      |def createUser(name: String, age: Int): User can Throw[UserError] =
      |  if name.isBlank then throw(UserError.BlankName)
      |  else if age < 0 then throw(UserError.NegativeAge)
      |  else User(name, age)
      |
      |test createUser with
      |  assertEquals(createUser("Sofia", 23), User("Sofia", 23))
      |  assertThrows(createUser("", 23), UserError.BlankName)
      |  assertThrows(createUser("Sofia", -23), UserError.NegativeAge)""".stripMargin
  )

def navbar(using translation: Translation) =
  div(
    cls := "fixed top-0 z-1 w-full",
    div(
      cls := "navbar bg-base-100 shadow-sm px-10 mb-5 min-h-0 h-15",
      div(
        cls := "navbar-start",
        a(cls := "font-bold text-2xl text-primary", "Algorab")
      ),
      ul(
        cls := "navbar-end menu menu-horizontal px-1 flex gap-1 text-xl",
        li(a(cls := "btn btn-primary", href := "#header", tr"index.subscribe")),
        li(
          details(
            summary(availableLanguages.getOrElse(translation.language, "?")),
            ul(
              cls := "bg-base-100 rounded-t-none p-2",
              availableLanguages.map((lang, icon) =>
                li(
                  a(
                    cls := "text-nowrap",
                    href := s"?lang=$lang",
                    s"$icon ${lang.toUpperCase}"
                  )
                )
              ).toList
            )
          )
        )
      )
    ),
    div(
      id := "alert_container",
      cls := "w-full flex flex-col items-center gap-3"
    )
  )

def headerPart(using lang: Translation) =
  header(
    id := "header",
    cls := "hero bg-base-200 h-screen pt-15",
    div(
      cls := "@container hero-content justify-evenly flex-col lg:flex-row w-full",
      div(
        cls := "basis-1/2",
        h1(cls := "text-center text-8xl font-bold text-primary py-6", "Algorab"),
        p(
          cls := "text-center text-3xl font-medium py-8",
          tr"index.slogan"
        ),
        form(
          id := "subscription_form",
          cls := "join w-full px-10 hidden lg:inline-flex",
          onsubmit := "return onSubmit(this)",
          input(`type` := "hidden", name := "lang", value := lang.language),
          div(
            cls := "w-full flex flex-col gap-2",
            label(
              cls := "input validator join-item w-full",
              svg(
                cls := "h-[1em] opacity-50",
                xmlns := "http://www.w3.org/2000/svg",
                viewBox := "0 0 24 24",
                g(
                  attr("stroke-linejoin") := "round",
                  attr("stroke-linecap") := "round",
                  attr("stroke-width") := "2.5",
                  fill := "none",
                  stroke := "currentColor",
                  rect(width := "20", height := "16", x := "2", y := "4", rx := "2"),
                  path(d := "m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7")
                )
              ),
              input(
                `type` := "email",
                name := "email",
                placeholder := "mail@site.com",
                required
              )
            ),
            a(
              cls := "link text-sm",
              onclick := "unsubscribe()",
              tr"index.unsubscribe"
            ),
            div(cls := "validator-hint hidden", tr"index.subscription.invalidemail")
          ),
          newsletterButton(tr"index.subscribe")(
            cls := "btn btn-primary join-item",
            tpe := "submit"
          )
        )
      ),
      headerCode
    )
  )

def pageSection(modifiers: Modifier*)(using Translation) =
  div(
    cls := "flex flex-row min-w-full p-10 justify-center bg-primary",
    div(
      cls := "max-w-5xl justify-center",
      modifiers
    )
  )

def descriptionPart(using Translation) =
  pageSection(
    p(
      cls := "text-xl text-primary-content text-justify",
      tr"index.description"
    )
  )

def featureHero(reversed: Boolean, sectionId: String)(codeModifiers: Modifier*)(using Translation) =
  val flexDir =
    if reversed then "lg:flex-row-reverse"
    else "lg:flex-row"

  div(
    id := sectionId,
    cls := "hero bg-base-200",
    div(
      cls := s"hero-content flex-col $flexDir gap-16 p-0",
      div(
        cls := "flex flex-col basis-1/2 items-center lg:items-start",
        h1(cls := "text-4xl", tr"index.$sectionId.title"),
        p(cls := "py-6 text-justify", tr"index.$sectionId.text")
        // button(cls := "btn btn-primary", tr"index.more")
      ),
      div(cls := "basis-1/2 min-w-0 w-full", codeModifiers)
    )
  )

def featureTable(using Translation) =
  val yes = td(
    div(
      cls := "flex flex-row justify-center",
      input(tpe := "checkbox", cls := "checkbox checkbox-primary", checked, onclick := "return false")
    )
  )

  val no = td(
    div(
      cls := "flex flex-row justify-center",
      input(tpe := "checkbox", cls := "checkbox checkbox-primary", onclick := "return false")
    )
  )

  def sortOf(reason: String) = td(
    div(
      cls := "flex flex-row justify-center tooltip",
      attr("data-tip") := reason,
      input(tpe := "checkbox", cls := "checkbox checkbox-primary indeterminate", onclick := "return false")
    )
  )

  div(
    cls := "w-full flex flex-col items-center gap-5",
    h1(cls := "text-4xl text-center", tr"index.table.title"),
    div(
      cls := "w-full overflow-x-auto",
      table(
        cls := "table lg:table-lg max-w-full",
        thead(
          cls := "lg:text-lg",
          trow(
            th(tr"index.table.feature"),
            th(
              div(
                cls := "flex flex-row justify-center items-center gap-2",
                img(
                  cls := "size-6",
                  src := "/public/img/python.png"
                ),
                "Python"
              )
            ),
            th(
              div(
                cls := "flex flex-row justify-center items-center gap-2",
                img(
                  cls := "size-6",
                  src := "/public/img/c.png"
                ),
                "C"
              )
            ),
            th(cls := "text-center text-primary", "Algorab")
          )
        ),
        tbody(
          trow(
            td(tr"index.table.typing"),
            no,
            sortOf(tr"index.table.typing.c"),
            yes
          ),
          trow(
            td(tr"index.table.errors"),
            sortOf(tr"index.table.errors.python"),
            no,
            yes
          ),
          trow(
            td(tr"index.table.expressivity"),
            no,
            no,
            yes
          ),
          trow(
            td(tr"index.table.goodpractices"),
            no,
            no,
            yes
          ),
          trow(
            td(tr"index.table.ecosystem"),
            yes,
            yes,
            no
          )
        )
      )
    ),
    label(cls := "text-md self-start", tr"index.table.soon")
  )


def featuresPart(using Translation) =
  div(
    cls := "w-full flex flex-row justify-center py-16 px-10 bg-base-200",
    div(
      cls := "flex flex-col gap-16 max-w-5xl",
      featureHero(false, "quickstart")(
        cls := "flex flex-col gap-3",
        tabs(
          cls := "@container w-full tabs-lift",

          radioTab("Algorab")(checked := true),
          quickStartCode,

          radioTab("Python")(),
          quickStartPythonCode,

          radioTab("C")(),
          quickStartCCode
        ),
        prefixedCode(cls := "overflow-x-auto")(
          "" -> "Hello World",
          "" -> "What's your name?",
          ">" -> "Alice",
          "" -> "Hello Alice!"
        )
      ),
      featureHero(true, "expressivity")(
        tabs(
          cls := "@container w-full tabs-lift",
          radioTab("Algorab")(checked := true),
          expressivityCode,

          radioTab("Python")(),
          expressivityPythonCode,

          radioTab("C")(),
          expressivityCCode
        )
      ),
      featureHero(false, "assistance")(
        cls := "@container w-full flex flex-col gap-3",
        assitanceCode,
        unprefixedCode(cls := "overflow-x-auto")(
          """Error: Unknown record field.
            |
            |At ./myscript.algo, line 4, character 22
            |println("Hello ${user.username} !")
            |                 ^^^^^^^^^^^^^
            |Hint: user is of type User.
            |      Did you mean user.name?""".stripMargin
        )
      ),
      featureHero(true, "professionalisation")(
        cls := "@container w-full",
        professionalisationCode
      ),
      featureTable
    )
  )

def openSourcePart(using Translation) =
  pageSection(
    cls := "flex flex-col gap-5",
    h1(
      cls := "text-4xl text-primary-content text-center",
      tr"index.opensource.title"
    ),
    p(
      cls := "text-xl text-primary-content text-justify",
      tr"index.opensource.text"
    )
  )

def indevPart(using lang: Translation) =
  div(
    cls := "flex flex-row min-w-full p-10 bg-base-200 justify-center",
    div(
      cls := "flex flex-col max-w-5xl gap-5 justify-center items-center",
      h1(
        cls := "text-4xl text-center",
        tr"index.indev.title"
      ),
      p(
        cls := "text-xl text-center",
        tr"index.indev.text"
      ),
      form(
        id := "subscription_form",
        cls := "join max-w-2xl px-10 py-10",
        onsubmit := "return onSubmit(this)",
        input(`type` := "hidden", name := "lang", value := lang.language),
        div(
          cls := "w-full flex flex-col gap-2",
          label(
            cls := "input validator join-item w-full",
            svg(
              cls := "h-[1em] opacity-50",
              xmlns := "http://www.w3.org/2000/svg",
              viewBox := "0 0 24 24",
              g(
                attr("stroke-linejoin") := "round",
                attr("stroke-linecap") := "round",
                attr("stroke-width") := "2.5",
                fill := "none",
                stroke := "currentColor",
                rect(width := "20", height := "16", x := "2", y := "4", rx := "2"),
                path(d := "m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7")
              )
            ),
            input(
              `type` := "email",
              name := "email",
              placeholder := "mail@site.com",
              required
            )
          ),
          a(
            cls := "link text-sm",
            onclick := "unsubscribe()",
            tr"index.unsubscribe"
          ),
          div(cls := "validator-hint hidden", tr"index.subscription.invalidemail")
        ),
        newsletterButton(tr"index.subscribe")(
          cls := "btn btn-primary join-item",
          tpe := "submit"
        )
      )
    )
  )

def footerPart(using Translation) =
  footer(
    cls := "footer footer-horizontal footer-center bg-base-200 p-10",
    aside(
      p(cls := "font-bold", "Algorab"),
      p(s"Copyright © ${Year.now} - All right reserved")
    )
  )

def home(using Translation) =
  html(
    head(
      titleTag("Algorab | Home"),
      meta(
        name := "viewport",
        content := "width=device-width, initial-scale=1.0",
        charset := "UTF-8"
      ),
      link(rel := "stylesheet", href := "/public/output.css"),
      script(src := "/public/js/index.js")
    ),
    body(
      navbar,
      headerPart,
      descriptionPart,
      featuresPart,
      openSourcePart,
      indevPart,
      footerPart
    )
  )
