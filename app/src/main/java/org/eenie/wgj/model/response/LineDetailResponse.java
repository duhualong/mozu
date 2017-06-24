package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/23 at 12:41
 * Email: 472279981@qq.com
 * Des:
 */

public class LineDetailResponse {

    /**
     * turn : 33.33 完成率
     * turn_total : 10  点数
     * turn_actual : 5  完成的点数
     * number : 20
     * number_actual : 2
     * ring : 3   要巡检的圈数
     * turn_ring : 1  完成的圈数
     */

    private double turn;
    private int turn_total;
    private int turn_actual;
    private int number;
    private int number_actual;
    private int ring;
    private int turn_ring;

    public double getTurn() {
        return turn;
    }

    public void setTurn(double turn) {
        this.turn = turn;
    }

    public int getTurn_total() {
        return turn_total;
    }

    public void setTurn_total(int turn_total) {
        this.turn_total = turn_total;
    }

    public int getTurn_actual() {
        return turn_actual;
    }

    public void setTurn_actual(int turn_actual) {
        this.turn_actual = turn_actual;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber_actual() {
        return number_actual;
    }

    public void setNumber_actual(int number_actual) {
        this.number_actual = number_actual;
    }

    public int getRing() {
        return ring;
    }

    public void setRing(int ring) {
        this.ring = ring;
    }

    public int getTurn_ring() {
        return turn_ring;
    }

    public void setTurn_ring(int turn_ring) {
        this.turn_ring = turn_ring;
    }
}
