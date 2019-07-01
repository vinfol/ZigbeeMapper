package cn.edu.bupt.ZigbeeMapper.model;

public class Relation {
    private String version;

    private String snid;

    private String username;

    private String password;

    private Integer devicenumber;

    private Integer groupnumber;

    private Integer timernumber;

    private Integer scenenumber;

    private Integer missionnumber;

    private String compileversionnumber;

    public Relation(String version, String snid, String username, String password, Integer devicenumber, Integer groupnumber, Integer timernumber, Integer scenenumber, Integer missionnumber, String compileversionnumber) {
        this.version = version;
        this.snid = snid;
        this.username = username;
        this.password = password;
        this.devicenumber = devicenumber;
        this.groupnumber = groupnumber;
        this.timernumber = timernumber;
        this.scenenumber = scenenumber;
        this.missionnumber = missionnumber;
        this.compileversionnumber = compileversionnumber;
    }

    public Relation() {
        super();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public String getSnid() {
        return snid;
    }

    public void setSnid(String snid) {
        this.snid = snid == null ? null : snid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getDevicenumber() {
        return devicenumber;
    }

    public void setDevicenumber(Integer devicenumber) {
        this.devicenumber = devicenumber;
    }

    public Integer getGroupnumber() {
        return groupnumber;
    }

    public void setGroupnumber(Integer groupnumber) {
        this.groupnumber = groupnumber;
    }

    public Integer getTimernumber() {
        return timernumber;
    }

    public void setTimernumber(Integer timernumber) {
        this.timernumber = timernumber;
    }

    public Integer getScenenumber() {
        return scenenumber;
    }

    public void setScenenumber(Integer scenenumber) {
        this.scenenumber = scenenumber;
    }

    public Integer getMissionnumber() {
        return missionnumber;
    }

    public void setMissionnumber(Integer missionnumber) {
        this.missionnumber = missionnumber;
    }

    public String getCompileversionnumber() {
        return compileversionnumber;
    }

    public void setCompileversionnumber(String compileversionnumber) {
        this.compileversionnumber = compileversionnumber == null ? null : compileversionnumber.trim();
    }
}