package smartfoodcluster.feedme.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by Lavenger on 4/21/2016.
 */
@Entity
public class Weave {
    @Id
    Long id;

    boolean blinkFirst;
    boolean blinkSecond;

    boolean onFirst;
    boolean onSecond;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBlinkFirst() {
        return blinkFirst;
    }

    public void setBlinkFirst(boolean blinkFirst) {
        this.blinkFirst = blinkFirst;
    }

    public boolean isBlinkSecond() {
        return blinkSecond;
    }

    public void setBlinkSecond(boolean blinkSecond) {
        this.blinkSecond = blinkSecond;
    }

    public boolean isOnFirst() {
        return onFirst;
    }

    public void setOnFirst(boolean onFirst) {
        this.onFirst = onFirst;
    }

    public boolean isOnSecond() {
        return onSecond;
    }

    public void setOnSecond(boolean onSecond) {
        this.onSecond = onSecond;
    }
}

