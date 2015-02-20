/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut17_ambientLighting.util;

import com.jogamp.newt.event.KeyEvent;
import ogldevtutorials.tut17_ambientLighting.Tutorial17;


/**
 *
 * @author gbarbieri
 */
public class KeyListener implements com.jogamp.newt.event.KeyListener{

    
    @Override
    public void keyPressed(KeyEvent ke) {
     
        float step = 0.05f;
        
        switch(ke.getKeyCode()){
            
            case KeyEvent.VK_A:
                float ambientIntensity = Tutorial17.instance.getDirectionalLight().getAmbientIntensity();
                ambientIntensity += step;
                Tutorial17.instance.getDirectionalLight().setAmbientIntensity(ambientIntensity);
                break;
            
            case KeyEvent.VK_S:
                ambientIntensity = Tutorial17.instance.getDirectionalLight().getAmbientIntensity();
                ambientIntensity -= step;
                Tutorial17.instance.getDirectionalLight().setAmbientIntensity(ambientIntensity);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    
    }    
}
