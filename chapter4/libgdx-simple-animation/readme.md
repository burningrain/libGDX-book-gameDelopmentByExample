# LibGDX Simple Animation

A library for libGDX cross-platform game development framework.
Provides a lightweight ability to create animations by using an FSM description (as you do in Unity).

## Architecture
![solution architecture](https://github.com/burningrain/libGDX-book-gameDelopmentByExample/blob/master/chapter4/libgdx-simple-animation/doc/structure.png?raw=true)

### FSM (Finite state machine)
The finite state machine consists of:
* State
* Transition (contains ```FsmPredicate```)
* FsmContext

Every call ```FSM#update``` the state machine makes decision whether or not to change the current state.
The current state tests transition predicates. If a predicate returns ```true``` then the current state will be changed.
A predicate consists of conditions with ```FsmContext``` variables. Thus, we can control the fsm switching by 
changing the values of ```FsmContext``` variables.
If you want to create a transition from any state you should use ```ANY_STATE``` at the transition description. For example:
```json
{
  ...
  "transitions": [
    ...
    {
      "from": "ANY_STATE",
      "to": "ATTACK",
      "fsmPredicates": [
        "ATTACK == true"
      ]
    }
  ],
  ...
}
```

### Predicate Interpreter
Predicate Interpreter was created to describe transition conditions of a fsm at file ```.afsm```.
Interpreter can work with next instruction pattern:
```shell 
[VARIABLE][space][OPERATOR][space][VALUE] 
```
for example:
```shell
MOVEMENT >= 5.0
```

**Spaces are mandatory for correct parsing!**

* _Variable_ is a variable from the```FsmContext``` 
* _Supported operators:_ ```<```, ```<=```, ```>```, ```>=```, ```==```
* _Supported value types:_ ```Integer```, ```Float```, ```Boolean```

If you describe an array of predicates then will be considered through the logical AND. For example:
```json
"fsmPredicates": [
"ATTACK == false",
"MOVEMENT > 0"
]
```
equals to:
```shell
(ATTACK == false) && (MOVEMENT > 0)
```

### Animation
To reduce the memory footprint, an animation is derived on 2 parts: static and dynamic (pattern 'flyweight').
```SimpleAnimation``` is a static part that consists of:
* fsm
* texture atlas

```SimpleAnimationComponent``` is a dynamic part that contains parameters for switching the finite state machine by using ```FsmContext```.
If you want to know the moment of an animation end you can use ```com.github.br.gdx.simple.animation.component.SimpleAnimatorUtils.isAnimationFinished```

## Quick start
* add the dependency:
```groovy
dependencies {
    implementation project("com.github.br:libgdx-simple-animation:$version")
}
```

* create next file structure at your assets/resources:
```shell
assets
|__ folder
    |__animation.png    # a texture
    |__animation.atlas  # an atlas
    |__animation.afsm   # an animation finite state machine description
```

* Set loader to AssetManager and load an animation:
```java
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.SimpleAnimationSyncLoader;
import com.github.br.gdx.simple.animation.SimpleAnimationSystem;
import com.github.br.ecs.simple.system.animation.AnimationSystem;

...
        
FileHandleResolver resolver = new InternalFileHandleResolver();
assetManager.setLoader(SimpleAnimation.class, new SimpleAnimationSyncLoader(resolver));

AssetManager assetManager = new AssetManager(resolver);
assetManager.load("folder/animation.afsm", SimpleAnimation.class);
assetManager.finishLoading();

SimpleAnimationSystem animationSystem = new SimpleAnimationSystem();
animationSystem.addAnimation(assetManager.<SimpleAnimation>get("folder/animation.afsm"));
```

* create SimpleAnimationComponent and update its parameters whenever you wish:
```java
import com.github.br.gdx.simple.animation.component.SimpleAnimationComponent;
import com.github.br.gdx.simple.animation.fsm.FsmContext;
import com.github.br.gdx.simple.animation.component.AnimatorDynamicPart;
...
FsmContext fsmContext = new FsmContext();

// adding animation variables. Supported types: Integer, Float, Boolean
fsmContext.insert("MOVEMENT", 0f);
fsmContext.insert("ATTACK", false);
fsmContext.insert("JUMP", false);
fsmContext.insert("FLY", false);

AnimatorDynamicPart animatorDynamicPart = new AnimatorDynamicPart();
SimpleAnimationComponent simpleAnimationComponent = new SimpleAnimationComponent(
        "animation",
        fsmContext,
        animatorDynamicPart
);
```

* every a game frame you should to call the method ```SimpleAnimationSystem#update```:
```java
private final SimpleAnimationSystem animationSystem = new SimpleAnimationSystem();

@Override
public void render(float delta) {
    animationSystem.update(delta, simpleAnimationComponent);
}
```

* last but not least. Don't forget to call ```SimpleAnimationSystem#dispose``` to free up animation resources.

#### .AFSM
```json
{
  "name": "crab",
  "animators": [
    {
      "name": "MOVEMENT",
      "frameFrequency": 60,
      "mode": "LOOP",
      "from": 0,
      "to": 45,
      "looping": true
    },
    {
      "name": "ATTACK",
      "frameFrequency": 30,
      "mode": "NORMAL",
      "from": 45,
      "to": 76,
      "looping": false
    },
    {
      "name": "JUMP",
      "frameFrequency": 60,
      "mode": "NORMAL",
      "from": 76,
      "to": 87,
      "looping": false
    },
    {
      "name": "FLY",
      "frameFrequency": 30,
      "mode": "LOOP",
      "from": 87,
      "to": 105,
      "looping": true
    },
    {
      "name": "IDLE",
      "frameFrequency": 10,
      "mode": "LOOP",
      "from": 76,
      "to": 87,
      "looping": true
    }
  ],
  "fsm": {
    "states": [
      "IDLE",
      "MOVEMENT",
      "ATTACK",
      "JUMP",
      "FLY"
    ],
    "startState": "IDLE",
    "endState": "",
    "variables": {
      "MOVEMENT": "Float",
      "JUMP": "Boolean",
      "FLY": "Boolean",
      "ATTACK": "Boolean"
    },
    "transitions": [
      {
        "from": "IDLE",
        "to": "MOVEMENT",
        "fsmPredicates": [
          "MOVEMENT > 0"
        ]
      },
      {
        "from": "MOVEMENT",
        "to": "IDLE",
        "fsmPredicates": [
          "MOVEMENT <= 0"
        ]
      },
      {
        "from": "IDLE",
        "to": "JUMP",
        "fsmPredicates": [
          "JUMP == true"
        ]
      },
      {
        "from": "MOVEMENT",
        "to": "JUMP",
        "fsmPredicates": [
          "JUMP == true"
        ]
      },
      {
        "from": "JUMP",
        "to": "FLY",
        "fsmPredicates": [
          "FLY == true"
        ]
      },
      {
        "from": "IDLE",
        "to": "FLY",
        "fsmPredicates": [
          "FLY == true"
        ]
      },
      {
        "from": "MOVEMENT",
        "to": "FLY",
        "fsmPredicates": [
          "FLY == true"
        ]
      },
      {
        "from": "FLY",
        "to": "IDLE",
        "fsmPredicates": [
          "FLY == false",
          "MOVEMENT <= 0"
        ]
      },
      {
        "from": "FLY",
        "to": "MOVEMENT",
        "fsmPredicates": [
          "FLY == false",
          "MOVEMENT > 0"
        ]
      },
      {
        "from": "ANY_STATE",
        "to": "ATTACK",
        "fsmPredicates": [
          "ATTACK == true"
        ]
      },
      {
        "from": "ATTACK",
        "to": "IDLE",
        "fsmPredicates": [
          "ATTACK == false",
          "MOVEMENT <= 0"
        ]
      },
      {
        "from": "ATTACK",
        "to": "MOVEMENT",
        "fsmPredicates": [
          "ATTACK == false",
          "MOVEMENT > 0"
        ]
      },
      {
        "from": "ATTACK",
        "to": "FLY",
        "fsmPredicates": [
          "ATTACK == false",
          "FLY == true"
        ]
      }
    ]
  }
}
```

## License
Code is licensed under MIT license