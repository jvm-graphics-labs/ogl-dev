/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut19_specularLighting;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author gbarbieri
 */
public class Tutorial19 {
    
    public static void main(String[] args) {

        final Viewer viewer = new Viewer();

        instance = viewer;

        final Frame frame = new Frame("Tutorial 19");

        frame.add(viewer.getNewtCanvasAWT());

        frame.setSize(viewer.getGlWindow().getWidth(), viewer.getGlWindow().getHeight());

        frame.setLocation(100, 100);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                viewer.getAnimator().stop();
                viewer.getGlWindow().destroy();
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);
    }

    public static Viewer instance;
}
