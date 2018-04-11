package com.example.shesha.tourpal;

class Climate {
    private String stationname;
    private String month;
    private String period;
    private String noofyears;
    private Double meantempmax;
    private Double meantempmin;
    private Double rainfall;

    public Climate() {
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getNoofyears() {
        return noofyears;
    }

    public void setNoofyears(String noofyears) {
        this.noofyears = noofyears;
    }

    public Double getMeantempmax() {
        return meantempmax;
    }

    public void setMeantempmax(Double meantempmax) {
        this.meantempmax = meantempmax;
    }

    public Double getMeantempmin() {
        return meantempmin;
    }

    public void setMeantempmin(Double meantempmin) {
        this.meantempmin = meantempmin;
    }

    public Double getRainfall() {
        return rainfall;
    }

    public void setRainfall(Double rainfall) {
        this.rainfall = rainfall;
    }
}
