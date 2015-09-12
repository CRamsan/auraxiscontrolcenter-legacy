package com.cesarandres.ps2link.dbg.content.item;

public class WeaponStat implements Comparable<WeaponStat> {

    private String name;
    private String vehicle;
    private String imagePath;
    private int kills;
    private int TR;
    private int VS;
    private int NC;
    private int headshots;
    private int vehicleKills;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getTR() {
        return TR;
    }

    public void setTR(int tR) {
        TR = tR;
    }

    public int getVS() {
        return VS;
    }

    public void setVS(int vS) {
        VS = vS;
    }

    public int getNC() {
        return NC;
    }

    public void setNC(int nC) {
        NC = nC;
    }

    public int getHeadshots() {
        return headshots;
    }

    public void setHeadshots(int headshots) {
        this.headshots = headshots;
    }

    public int getVehicleKills() {
        return vehicleKills;
    }

    public void setVehicleKills(int vehicleKills) {
        this.vehicleKills = vehicleKills;
    }

    @Override
    public int compareTo(WeaponStat another) {
        if (another.getKills() - this.getKills() == 0) {
            if (another.getHeadshots() - this.getHeadshots() == 0) {
                return another.getVehicleKills() - this.getVehicleKills();
            } else {
                return another.getHeadshots() - this.getHeadshots();
            }
        } else {
            return another.getKills() - this.getKills();
        }
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}
