package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.TextAdventureModel;

public class TakeAnItem implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private List<Item> items;

    public TakeAnItem( List<Item> items ) {
        this.items = items;
        for( Item item : items )
            followUpActions.add( new TakeSpecificItem( item, null ) );
    }

    public List<Item> items() {
       return items;
    }

    public String label() {
        return "Take an item";
    }

    public void trigger() {
    }

    public boolean userMustChooseFollowUpAction() {
        return true;
    }

    public List<Action> followUpActions() {
        return followUpActions;
    }

    public boolean userTextAvailable() {
        return false;
    }

    public String userText() {
        return "";
    }

    @Override
    public boolean equals( Object o ) {
        if( !(o instanceof TakeAnItem) )
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}


