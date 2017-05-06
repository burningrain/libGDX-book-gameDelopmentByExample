package com.packt.flappeebee.render.layers;


public abstract class Layer {

    private final String title;
    private boolean hide = false;

    public Layer(String title){
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public abstract void draw();

    public boolean isHide() {
        return hide;
    }

    public void hide() {
        this.hide = true;
    }

    public void show() {
        this.hide = false;
    }

    void render(){
        if(!isHide()){
            draw();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Layer layer = (Layer) o;

        return !(title != null ? !title.equals(layer.title) : layer.title != null);

    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
