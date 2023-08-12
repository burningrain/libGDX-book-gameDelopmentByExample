* TextNode отображает текст
* ChooserNode отображает ноду выбора варианта ответа
* CustomNode делает то, что прописал пользователь. Возможно движение камеры, применение шейдера, 
             обновление состояния контекста/модели и так далее.


автомат переключается в новое состояние только тогда, когда закончил воспроизведение текущего состояния.
Окончание состояния в ui части определяет состояние ui. 
"Состояние автомата ui" к "состояние автомата текста" = 1 к 1
Автомат же в текстовой части визуальной новеллы переключается сразу. 

!!! не забыть помечать посещенные ноды и уже прощелканные ответы
!!! подумать о памяти

Character AUTHOR = Character.of("", keyValueMap1);
Character SMALL_PI = Character.of("Small Pi", keyValueMap1).animations("shrug", "smile");
Character ASTRONAUT_K = Character.of("Astronaut K.", keyValueMap1).animations("yell", "lookAt");
Character SOLDIER =  Character.of("Soldier Commander", keyValueMap1).animations("yell", "lookAt");

// имя ноды опционально. Можно не указывать. В случае отсутствия будет сгенерировано автоматоматически
// node("node1").to("node2") == node().to() == отсутствие to(). 
// .to() - переход к другой ноде внутри сцены или вообще другой сцене. Поиск всегда сначала идет внутри сцены.
// .to() применяется к текущей node и возвращает ссылку на изначальный билдер.  .node() меняет текущую ноду
Scene<ScreenManager> scene1 = Scene<ScreenManager>.builder()
         // .title("scene1")
         // .defaultLocale(Locale.EN)
         // .setScreenManager(new ScreenManager())

         .node(TextNode.builder().character(AUTHOR).text("astronaut K. home 5:30 am").build())
         .node(CameraNode.builder().moveTo(x1, y1).interpolate(ABC))
         .node(TextNode.builder().character(SOLDIER).animation("yell").text("This is police! Astronaut K., Open the door!!!").build())
         .node(CameraNode.builder().moveTo(x2, y2).interpolate(ABC).build())
         .node(TextNode.builder().character(ASTRONAUT_K).animation("yell").text("I'm giving you 4 seconds to apologize and leave!").build())
         .changeScreen((ScreenManager screen) -> {...TODO ...})
         .node(TextNode.builder().character(SOLDIER).animation("yell").text("Don't make my leg laugh, K. Give up! Open the door!").build())
         .node(ChooserNode.builder()
                .time(4_000) // время на ответ в миллисекундах. Если ответа нет - переход к дефолтовому варианту
                .defaultChoice("nodeA0") // выбор в случае просроченного времени
                .textNode(TextNode.builder().character(SOLDIER).text("We aren't gonna wait! It's your last chance. Open the door, K.!").build())
                .answer("scene2", "be silent") // кладет в контекст переход к "scene2"
                .answer("scene3", "run")
                .answer("scene4", "attack soldiers")
                .build())
             .toScene("scene2")
             .toScene("scene3")
             .toScene("scene4", "node6")
         .build();


Scene scene2 = SceneDsl.title("scene2").build();

Scene scene3 = SceneDsl.title("scene3").build();

Scene scene4 = SceneDsl.title("scene4").build();

PlotDsl
   // перечисление всех возможных персонажей
   .characters(
       SMALL_PI,
       ASTRONAUT_K
   )
   .scenes(scene1, scene2, scene3, scene4)


// выбор ветки идет через контекст. Если явно не указано в контексте куда идти, то идем в следующую ноду. 
// Если не указано куда и нод больше одной - кидать исключение.
PlotContext context = new PlotContext();