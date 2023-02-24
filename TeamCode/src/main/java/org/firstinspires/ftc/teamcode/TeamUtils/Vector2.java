package org.firstinspires.ftc.teamcode.TeamUtils;

public class Vector2 {
    public double x,y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(double angle) {
        this.x = Math.cos(angle);
        this.y = Math.sin(angle);
    }

    public Vector2 multiply(double other) {
        return new Vector2(this.x*other, this.y*other);
    }

    public double dot(Vector2 other) {
        return this.x*other.x+this.y*other.y;
    }

    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    public double angle() {
        return Math.atan2(this.y, this.x);
    }

    public Vector2 normalized() {
        double mag = this.magnitude();
        return new Vector2(this.x/mag, this.y/mag);
    }
}
