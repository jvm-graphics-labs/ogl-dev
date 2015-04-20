/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut22_jAssimp.util;

import com.jogamp.newt.event.KeyEvent;
import ogldevtutorials.tut22_jAssimp.Tutorial22;

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
                float ambientIntensity = Tutorial22.instance.getDirectionalLight().ambientIntensity;
                ambientIntensity += step;
                Tutorial22.instance.getDirectionalLight().ambientIntensity = ambientIntensity;
                break;

            case KeyEvent.VK_S:
                ambientIntensity = Tutorial22.instance.getDirectionalLight().ambientIntensity;
                ambientIntensity -= step;
                Tutorial22.instance.getDirectionalLight().ambientIntensity = ambientIntensity;
                break;

            case KeyEvent.VK_Z:
                float diffuseIntensity = Tutorial22.instance.getDirectionalLight().diffuseIntensity;
                diffuseIntensity += step;
                Tutorial22.instance.getDirectionalLight().diffuseIntensity = diffuseIntensity;
                break;

            case KeyEvent.VK_X:
                diffuseIntensity = Tutorial22.instance.getDirectionalLight().diffuseIntensity;
                diffuseIntensity -= step;
                Tutorial22.instance.getDirectionalLight().diffuseIntensity = diffuseIntensity;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }
}
