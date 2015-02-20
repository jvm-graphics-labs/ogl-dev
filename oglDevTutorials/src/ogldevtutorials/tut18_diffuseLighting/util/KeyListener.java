/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.tut18_diffuseLighting.util;

import com.jogamp.newt.event.KeyEvent;
import ogldevtutorials.tut18_diffuseLighting.Tutorial18;


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
                float ambientIntensity = Tutorial18.instance.getDirectionalLight().getAmbientIntensity();
                ambientIntensity += step;
                Tutorial18.instance.getDirectionalLight().setAmbientIntensity(ambientIntensity);
                break;
            
            case KeyEvent.VK_S:
                ambientIntensity = Tutorial18.instance.getDirectionalLight().getAmbientIntensity();
                ambientIntensity -= step;
                Tutorial18.instance.getDirectionalLight().setAmbientIntensity(ambientIntensity);
                break;
                
            case KeyEvent.VK_Z:
                float diffuseIntensity = Tutorial18.instance.getDirectionalLight().getDiffuseIntensity();
                diffuseIntensity += step;
                Tutorial18.instance.getDirectionalLight().setDiffuseIntensity(diffuseIntensity);
                break;
                
            case KeyEvent.VK_X:
                diffuseIntensity = Tutorial18.instance.getDirectionalLight().getDiffuseIntensity();
                diffuseIntensity -= step;
                Tutorial18.instance.getDirectionalLight().setDiffuseIntensity(diffuseIntensity);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    
    }    
}
