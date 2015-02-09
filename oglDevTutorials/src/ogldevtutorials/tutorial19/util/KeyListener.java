/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tutorial19.util;

import com.jogamp.newt.event.KeyEvent;
import ogldevtutorials.tutorial19.Tutorial19;

/**
 *
 * @author gbarbieri
 */
public class KeyListener implements com.jogamp.newt.event.KeyListener {

    @Override
    public void keyPressed(KeyEvent ke) {

        float step = 0.05f;

        switch (ke.getKeyCode()) {

            case KeyEvent.VK_A:
                float ambientIntensity = Tutorial19.instance.getDirectionalLight().getAmbientIntensity();
                ambientIntensity += step;
                Tutorial19.instance.getDirectionalLight().setAmbientIntensity(ambientIntensity);
                break;

            case KeyEvent.VK_S:
                ambientIntensity = Tutorial19.instance.getDirectionalLight().getAmbientIntensity();
                ambientIntensity -= step;
                Tutorial19.instance.getDirectionalLight().setAmbientIntensity(ambientIntensity);
                break;

            case KeyEvent.VK_Z:
                float diffuseIntensity = Tutorial19.instance.getDirectionalLight().getDiffuseIntensity();
                diffuseIntensity += step;
                Tutorial19.instance.getDirectionalLight().setDiffuseIntensity(diffuseIntensity);
                break;

            case KeyEvent.VK_X:
                diffuseIntensity = Tutorial19.instance.getDirectionalLight().getDiffuseIntensity();
                diffuseIntensity -= step;
                Tutorial19.instance.getDirectionalLight().setDiffuseIntensity(diffuseIntensity);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }
}
