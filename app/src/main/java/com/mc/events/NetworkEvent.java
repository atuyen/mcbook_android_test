package com.mc.events;

public class NetworkEvent {
    private boolean haveNetwork;

    public NetworkEvent(boolean haveNetwork) {
        this.haveNetwork = haveNetwork;
    }

    public boolean isHaveNetwork() {
        return haveNetwork;
    }

    public void setHaveNetwork(boolean haveNetwork) {
        this.haveNetwork = haveNetwork;
    }
}
