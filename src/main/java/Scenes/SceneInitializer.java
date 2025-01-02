package Scenes;

import Scenes.Scene;

import java.util.List;

public abstract class SceneInitializer {
    public abstract void init(Scene scene);
    public abstract void loadResources(Scene scene);
    public abstract void imgui();
}
