package com.chewielouie.textadventure;

import java.util.HashSet;
import java.util.Set;

public class MovementMonitor implements MovementEventSubscriber {
    private Set<String> exploredAreas = new HashSet<String>();
    private TextAdventureModel model = null;

    public MovementMonitor( TextAdventureModel model ) {
        this.model = model;
        this.model.subscribeForEvents( this );
    }

    public void currentLocationChanged() {
        exploredAreas.add( model.currentLocation().areaID() );
    }

    public Set<String> exploredAreas() {
        return this.exploredAreas;
    }
}
