/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ogldevtutorials.util;

/**
 *
 * @author gbarbieri
 */
public class PersProjInfo {

    public float fov;
    public float width;
    public float height;
    public float zNear;
    public float zFar;

    public PersProjInfo(float fov, float width, float height, float zNear, float zFar) {

        this.fov = fov;
        this.width = width;
        this.height = height;
        this.zNear = zNear;
        this.zFar = zFar;
    }
}
