package com.packt.snake.render.layers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class LayerFolder extends Layer {

    private ArrayList<Layer> layers = new ArrayList<>();
    private HashMap<String, Layer> layersMap = new HashMap<>();

    public LayerFolder(String title){
        super(title);
    }

    public void hideLayer(String title){
        layersMap.get(title).hide();
    }

    public void showLayer(String title){
        layersMap.get(title).show();
    }

    public void addLayer(Layer layer){
        layers.add(layer);
        layersMap.put(layer.getTitle(), layer);
    }

    public void deleteLayer(String title){
        Iterator<Layer> iterator = layers.iterator();
        while (iterator.hasNext()){
            Layer next = iterator.next();
            if(title.equals(next.getTitle())){
                iterator.remove();
                break;
            }
        }
        layersMap.remove(title);
    }

    @Override
    public void draw() {
        for(Layer layer : layers){
            layer.render();
        }
    }



}
