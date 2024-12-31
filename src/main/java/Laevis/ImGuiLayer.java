package Laevis;


import Renderer.PickingTexture;
import editor.GameViewWindow;
import editor.PropertiesWindow;
import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import Scenes.Scene;
import imgui.type.ImBoolean;

import static org.lwjgl.glfw.GLFW.*;


public class   ImGuiLayer {
    public ImGuiImplGlfw imguiglfw;
    public ImGuiImplGl3 implGl3;
    private long glfwWindow;

    //mouse cursors provided by glfw
    private final long[] mousecursors=new long[ImGuiMouseCursor.COUNT];


    //flags for showing windows ,can be edited for any additions
    public boolean showText = false;
    public boolean showmainmenu=true ;
    public boolean show2dWin=false;
    public boolean show3dWin=false;


    private GameViewWindow gameViewWindow;

    private PropertiesWindow propertiesWindow;

    // Constructor initializes the ImGui backend
    public ImGuiLayer(long glfwWindow , PickingTexture pickingTexture) {
        this.glfwWindow=glfwWindow;
        this.gameViewWindow=new GameViewWindow();
        this.propertiesWindow=new PropertiesWindow(pickingTexture);
    }

    // Initialization method for ImGui
    public void initImGui() {
        imguiglfw = new ImGuiImplGlfw();
        implGl3 = new ImGuiImplGl3();

        // Create ImGui context
        //essential for imgui to work
        ImGui.createContext();

        //init ImguiIo config
        final ImGuiIO io = ImGui.getIO();
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors);
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.setBackendPlatformName("imgui_java_impl_glfw");//i have no idea what this does
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        io.setDisplaySize(1920, 1080);
        io.getFonts().addFontDefault();
        io.getFonts().build();

        // ------------------------------------------------------------
        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
        io.setKeyMap(keyMap);
        // ------------------------------------------------------------
        // Mouse cursors mapping
        mousecursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mousecursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mousecursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mousecursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mousecursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mousecursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mousecursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mousecursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mousecursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

        // ------------------------------------------------------------

        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            if (!io.getWantCaptureKeyboard()) {
                KeyListener.keyCallback(w, key, scancode, action, mods);
            }
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }
            if(!io.getWantCaptureMouse() || gameViewWindow.getWantCaptureMouse()){
                MouseListener.mouseButtonCallback(w,button,action,mods);
            }


        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
        });




        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });
        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        implGl3.init("#version 330 core");
    }



    public void update(float dt, Scene currentScene) {
        startFrame(dt);

        // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
        ImGui.newFrame();
        setupDockspace();
        currentScene.imgui();
        ImGui.showDemoWindow();
        gameViewWindow.imgui();
        propertiesWindow.imgui();
        propertiesWindow.update(dt,currentScene);
        ImGui.end();
        ImGui.render();

        endFrame();
    }

    private void startFrame(final float deltaTime) {
        // Get window properties and mouse position
        float[] winWidth = {Window.getWidth()};
        float[] winHeight = {Window.getHeight()};
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(glfwWindow, mousecursors[imguiCursor]);
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    private void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        implGl3.renderDrawData(ImGui.getDrawData());
    }

    // If you want to clean a room after yourself - do it by yourself
    private void destroyImGui() {
        implGl3.dispose();
        ImGui.destroyContext();
    }


    // Call this method to render ImGui elements
    public void ImGui() {


        if(showmainmenu) {
            // Set window size (width, height) and position (x, y)
            ImGui.setWindowSize(400, 300);  // Set the window size to 400x300
            ImGui.setWindowPos(100, 100);   // Set the window position to (100, 100)

            ImGui.begin("Main menu");

            ImGui.showDemoWindow();

            if (ImGui.button("2d", 500, 50)) {

                show2dWin = true;

            }
            if (ImGui.button("3d", 550, 50)) {
                show3dWin = true;
            }
            ImGui.end();
        }
        if(show2dWin){
            ImGui.begin("2d editor");
            ImGui.text("this is the 2d editor");
            showmainmenu=false;
            if(ImGui.button("return to main menu")){
                showmainmenu=true;
            }
            ImGui.end();
        }

        if(show3dWin){
            ImGui.begin("3d editor");
            ImGui.text("this is the 3d editor");

            ImGui.end();
        }
        if (showText) {
            ImGui.text("This is a cool message!");
            ImGui.sameLine();
        }


    }


    private void setupDockspace(){
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGui.setNextWindowPos(0.0f,0.0f,ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding,0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize,0.0f);
        windowFlags|=ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse
                | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove|
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

          ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
          ImGui.popStyleVar(2);

          //DAH BEYE3MEL DOCKSPACE
          ImGui.dockSpace(ImGui.getID("Dockspace"));


    }



}
