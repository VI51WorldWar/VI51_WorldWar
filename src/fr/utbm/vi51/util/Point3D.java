package fr.utbm.vi51.util;

import javax.vecmath.Point3d;

/**
 * @author Top-K
 * 
 */
public class Point3D {
    public int x;
    public int y;
    public int z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point3d p) {
        this.x = (int) Math.floor(p.x);
        this.y = (int) Math.floor(p.y);
        this.z = (int) Math.floor(p.z);
    }

    public String toString() {
        return "x:" + x + " y:" + y + " z:" + z;
    }

    public boolean equals(Point3D p) {
        return this.x == p.x && this.y == p.y && this.z == p.z;
    }
}
