package online.iizvv.pojo;

public class Authorize {

    private String csr;

    private String p8;

    private String iss;

    private String kid;


    public Authorize(String p8, String iss, String kid, String csr) {
        this.p8 = p8;
        this.iss = iss;
        this.kid = kid;
        this.csr = csr;
    }

    public Authorize(String p8, String iss, String kid) {
        this.p8 = p8;
        this.iss = iss;
        this.kid = kid;
    }

    public String getP8() {
        return p8;
    }

    public void setP8(String p8) {
        this.p8 = p8;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    @Override
    public String toString() {
        return "Authorize{" +
                "csr='" + csr + '\'' +
                ", p8='" + p8 + '\'' +
                ", iss='" + iss + '\'' +
                ", kid='" + kid + '\'' +
                '}';
    }
}
