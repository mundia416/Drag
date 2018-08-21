# Drag
enables overlays to be draggable on the screen by the users touch

## Usage

add jitpack to your project dependency
```
repositories {
     maven { url='https://jitpack.io'}
}    
```

add the library to your module dependency
```
dependencies {
    implementation 'com.github.mundia416:Drag:0.1.2'
}
```


the DraggableOverlayOnTouchListener can only work if used in the DraggableOverlayService. create a subclass that
extends the DraggableOverlayService.

create a draggable overlay onTouchListener
```
    val onTouchListener = DraggableOverlayOnTouchListener(inflatedOverlay,overlayParams)
```
set it as the OnTouchListener for the inflated view. you can also set it as the onTouchListener for the child views of the inflated 
view or you can create a seperate draggable overlay onTouchListener
```
        inflatedOverlay.setOnTouchListener(onTouchListener)
```

register the DraggableOverlayOnTouchListener with the DraggableOverlayService
```
override fun registerDraggableTouchListener() {
        registerOnTouchListener(onTouchListener)
}
```
    
## Set OnClickListener

if a DraggableOnTouchListener is attached to a view, any onClick listener currently attacked to the same view will not work.
the onClickListener will have to be attacked to the DraggableOnTouchListener
```
                onTouchListener.setOnClickListener(View.OnClickListener {  })
```

## Invert
you can set the movement of the overlay to be inverted
```
                onTouchListener.setInverseX(true)
                onTouchListener.setInverseY(true)
```

## Author

Mundia Mundia 



## License

Copyright 2018 Mundia Mundia

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

