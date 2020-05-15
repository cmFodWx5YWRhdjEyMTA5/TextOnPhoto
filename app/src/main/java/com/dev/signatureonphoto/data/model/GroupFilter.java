package com.dev.signatureonphoto.data.model;

public class GroupFilter {
    boolean purchased;

    public GroupFilter() {
    }

    public GroupFilter(boolean purchased) {
        this.purchased = purchased;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public String toString() {
        return "GroupFilter{" +
                "purchased=" + purchased +
                '}';
    }
}
