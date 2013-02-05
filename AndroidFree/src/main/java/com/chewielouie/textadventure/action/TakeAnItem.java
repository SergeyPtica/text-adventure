package com.chewielouie.textadventure.action;

import java.util.ArrayList;
import java.util.List;
import com.chewielouie.textadventure.Item;
import com.chewielouie.textadventure.TextAdventureModel;
import com.chewielouie.textadventure.UserInventory;

public class TakeAnItem implements Action {
    private List<Action> followUpActions = new ArrayList<Action>();
    private List<Item> items;
    private UserInventory inventory;

    public TakeAnItem( List<Item> items, UserInventory inventory ) {
        this.items = items;
        this.inventory = inventory;
        for( Item item : items )
            followUpActions.add( new TakeSpecificItem( item, inventory ) );
    }

    public List<Item> items() {
       return items;
    }

    public UserInventory inventory() {
        return inventory;
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

