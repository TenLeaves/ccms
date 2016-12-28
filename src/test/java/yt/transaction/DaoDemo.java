package yt.transaction;

import java.util.Map;

import org.n3r.eql.Eql;

public class DaoDemo {
    private Eql eql= new Eql();

    public Eql getEql() {
        return eql;
    }

    public void setEql(Eql eql) {
        this.eql = eql;
    }

    public void update(Map paraMap) {
        eql.update("updateOccuy").params(paraMap).execute();
    }
    
}
