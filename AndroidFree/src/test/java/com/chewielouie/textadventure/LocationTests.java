package com.chewielouie.textadventure;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.chewielouie.textadventure.action.Action;
import com.chewielouie.textadventure.action.ActionFactory;
import com.chewielouie.textadventure.action.TalkToAction;
import com.chewielouie.textadventure.item.Item;
import com.chewielouie.textadventure.item.NormalItem;
import java.util.ArrayList;
import java.util.List;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class LocationTests {

    private Mockery mockery = new Mockery();

    Location createLocation() {
        return new Location( "", "", null, null );
    }

    @Test
    public void id_is_set_on_construction() {
        Location l = new Location( "id", "", null, null );
        assertEquals( "id", l.id() );
    }

    @Test
    public void description_is_set_on_construction() {
        Location l = new Location( "", "description", null, null );
        assertEquals( "description", l.description() );
    }

    @Test
    public void added_exit_makes_the_exit_exitable() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( exit ).visible();
            will( returnValue( true ) );
            ignoring( exit );
        }});
        Location l = createLocation();
        l.addExit( exit );
        assertTrue( l.exitable( exit ) );
    }

    @Test
    public void non_visible_exits_are_not_exitable() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( exit ).visible();
            will( returnValue( false ) );
            ignoring( exit );
        }});
        Location l = createLocation();
        l.addExit( exit );
        assertFalse( l.exitable( exit ) );
    }

    @Test
    public void non_visible_exits_are_not_in_the_exits_list() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( exit ).visible();
            will( returnValue( false ) );
            ignoring( exit );
        }});
        Location l = createLocation();
        l.addExit( exit );
        assertEquals( 0, l.visibleExits().size() );
    }

    @Test
    public void exits_that_havent_been_added_are_not_exitable() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            ignoring( exit );
        }});
        Location l = createLocation();
        assertFalse( l.exitable( exit ) );
    }

    @Test
    public void exit_destination_is_retrievable() {
        final Exit exit = mockery.mock( Exit.class );
        mockery.checking( new Expectations() {{
            allowing( exit ).destination();
            will( returnValue( "destination" ) );
            ignoring( exit );
        }});
        Location l = createLocation();
        assertEquals( "destination", l.exitDestinationFor( exit ) );
    }

    @Test
    public void all_valid_exits_are_retrieveable() {
        final Exit exit1 = mockery.mock( Exit.class, "exit1" );
        final Exit exit2 = mockery.mock( Exit.class, "exit2" );
        mockery.checking( new Expectations() {{
            allowing( exit1 ).visible();
            will( returnValue( true ) );
            ignoring( exit1 );
            allowing( exit2 ).visible();
            will( returnValue( true ) );
            ignoring( exit2 );
        }});
        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit1 );
        exits.add( exit2 );

        Location l = createLocation();
        l.addExit( exit1 );
        l.addExit( exit2 );

        assertEquals( exits, l.visibleExits() );
    }

    @Test
    public void added_items_are_retrieveable() {
        List<Item> items = new ArrayList<Item>();
        items.add( new NormalItem() );

        Location l = createLocation();
        l.addItem( new NormalItem() );

        assertEquals( items, l.items() );
    }

    @Test
    public void actions_uses_action_factory_to_create_TakeAnItem_action_for_all_visible_takeable_items() {
        final Item visibleItem = mockery.mock( Item.class, "visible item" );
        final Item visibleUntakeableItem = mockery.mock( Item.class, "visible untakeable item" );
        final Item invisibleItem = mockery.mock( Item.class, "invisible item" );
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action takeAnItemAction = mockery.mock( Action.class );
        final List<Item> takeableItems = new ArrayList<Item>();
        takeableItems.add( visibleItem );
        final UserInventory inventory = mockery.mock( UserInventory.class );
        final Location l = new Location( "", "", inventory, actionFactory );
        mockery.checking( new Expectations() {{
            allowing( visibleItem ).visible(); will( returnValue( true ) );
            allowing( visibleItem ).takeable(); will( returnValue( true ) );
            ignoring( visibleItem );
            allowing( visibleUntakeableItem ).visible(); will( returnValue( true ) );
            allowing( visibleUntakeableItem ).takeable(); will( returnValue( false ) );
            ignoring( visibleUntakeableItem );
            allowing( invisibleItem ).visible(); will( returnValue( false ) );
            allowing( invisibleItem ).takeable(); will( returnValue( false ) );
            ignoring( invisibleItem );
            oneOf( actionFactory ).createTakeAnItemAction(
                with( equal( takeableItems ) ),
                with( equal( inventory ) ),
                with( equal( l ) ) );
            will( returnValue( takeAnItemAction ) );
            ignoring( actionFactory );
        }});
        l.addItem( visibleItem );
        l.addItem( invisibleItem );

        List<Action> actions = l.actions();
        assertTrue( actions.size() > 0 );
        assertThat( actions.get( 0 ), is( takeAnItemAction ) );
    }

    @Test
    public void location_action_to_take_an_item_is_not_included_if_only_untakeable_items_available() {
        final Item untakeableItem = mockery.mock( Item.class, "untakeable item" );
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Location l = new Location( "", "", null, actionFactory );
        mockery.checking( new Expectations() {{
            allowing( untakeableItem ).visible(); will( returnValue( true ) );
            allowing( untakeableItem ).takeable(); will( returnValue( false ) );
            ignoring( untakeableItem );
            never( actionFactory ).createTakeAnItemAction(
                with( any( List.class ) ),
                with( any( UserInventory.class ) ),
                with( any( ModelLocation.class ) ) );
            ignoring( actionFactory );
        }});
        l.addItem( untakeableItem );
        l.actions();
    }

    @Test
    public void actions_uses_action_factory_to_create_ExamineAnItem_action_for_all_visible_items() {
        final Item visibleItem = mockery.mock( Item.class, "visible item" );
        final Item invisibleItem = mockery.mock( Item.class, "invisible item" );
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Action examineAnItemAction = mockery.mock( Action.class );
        final List<Item> visibleItems = new ArrayList<Item>();
        visibleItems.add( visibleItem );
        mockery.checking( new Expectations() {{
            allowing( visibleItem ).visible(); will( returnValue( true ) );
            ignoring( visibleItem );
            allowing( invisibleItem ).visible(); will( returnValue( false ) );
            ignoring( invisibleItem );
            oneOf( actionFactory ).createExamineAnItemAction( with( equal( visibleItems ) ) );
            will( returnValue( examineAnItemAction ) );
            ignoring( actionFactory );
        }});
        Location l = new Location( "", "", null, actionFactory );
        l.addItem( visibleItem );
        l.addItem( invisibleItem );

        List<Action> actions = l.actions();
        assertTrue( actions.size() > 0 );
        assertThat( actions.get( 0 ), is( examineAnItemAction ) );
    }

    @Test
    public void actions_uses_action_factory_to_create_TalkTo_action_for_all_canTalkTo_items() {
        Item talkableItem = mock( Item.class );
        when( talkableItem.canTalkTo() ).thenReturn( true );
        when( talkableItem.visible() ).thenReturn( true );
        Item notTalkPhraseSourceItem = mock( Item.class );
        when( notTalkPhraseSourceItem.canTalkTo() ).thenReturn( false );
        when( notTalkPhraseSourceItem.visible() ).thenReturn( true );
        TalkToAction talkToAction = mock( TalkToAction.class );
        ActionFactory actionFactory = mock( ActionFactory.class );
        when( actionFactory.createTalkToAction( talkableItem ) ).thenReturn( talkToAction );

        Location l = new Location( "", "", null, actionFactory );
        l.addItem( talkableItem );
        l.addItem( notTalkPhraseSourceItem );

        assertThat( l.actions(), hasItem( talkToAction ) );
        verify( actionFactory, never() ).createTalkToAction( notTalkPhraseSourceItem );
    }

    @Test
    public void only_visible_items_can_be_spoken_to() {
        Item visibleItem = mock( Item.class );
        when( visibleItem.canTalkTo() ).thenReturn( true );
        when( visibleItem.visible() ).thenReturn( true );
        Item notVisibleItem = mock( Item.class );
        when( notVisibleItem.canTalkTo() ).thenReturn( true );
        when( notVisibleItem.visible() ).thenReturn( false );
        TalkToAction talkToAction = mock( TalkToAction.class );
        ActionFactory actionFactory = mock( ActionFactory.class );
        when( actionFactory.createTalkToAction( visibleItem ) ).thenReturn( talkToAction );

        Location l = new Location( "", "", null, actionFactory );
        l.addItem( visibleItem );
        l.addItem( notVisibleItem );

        assertThat( l.actions(), hasItem( talkToAction ) );
        verify( actionFactory, never() ).createTalkToAction( notVisibleItem );
    }

    @Test
    public void added_items_are_added_to_location_description() {
        Location l = new Location( "", "Location description.", null, null );
        NormalItem item1 = new NormalItem();
        item1.setName( "name" );
        l.addItem( item1 );
        NormalItem item2 = new NormalItem();
        item2.setName( "name2" );
        l.addItem( item2 );
        NormalItem item3 = new NormalItem();
        item3.setName( "name3" );
        l.addItem( item3 );

        assertEquals( "Location description.\nThere is a name, a name2 and a name3 here.\n", l.description() );
    }

    @Test
    public void description_includes_only_visible_items() {
        final Item visibleItem = mockery.mock( Item.class, "visible item" );
        final Item invisibleItem = mockery.mock( Item.class, "invisible item" );
        mockery.checking( new Expectations() {{
            allowing( visibleItem ).visible();
            will( returnValue( true ) );
            allowing( visibleItem ).countableNounPrefix();
            will( returnValue( "a" ) );
            allowing( visibleItem ).midSentenceCasedName();
            will( returnValue( "visible item" ) );
            ignoring( visibleItem );
            allowing( invisibleItem ).visible();
            will( returnValue( false ) );
            allowing( invisibleItem ).midSentenceCasedName();
            will( returnValue( "invisible item" ) );
            ignoring( invisibleItem );
        }});
        Location l = new Location( "", "Location description.", null, null );
        l.addItem( visibleItem );
        l.addItem( invisibleItem );

        assertEquals( "Location description.\nThere is a visible item here.\n", l.description() );
    }

    @Test
    public void removing_all_items_from_a_location_removes_TakeAnItem_action_from_action_list() {
        final Item takeableItem = mockery.mock( Item.class );
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Location l = new Location( "", "", null, actionFactory );
        mockery.checking( new Expectations() {{
            allowing( takeableItem ).visible(); will( returnValue( true ) );
            allowing( takeableItem ).takeable(); will( returnValue( true ) );
            ignoring( takeableItem );
            never( actionFactory ).createTakeAnItemAction(
                with( any( List.class ) ),
                with( any( UserInventory.class ) ),
                with( any( ModelLocation.class ) ) );
            ignoring( actionFactory );
        }});
        l.addItem( takeableItem );
        l.removeItem( takeableItem );
        l.actions();
    }

    @Test
    public void a_location_without_items_does_not_need_a_TakeAnItem_action() {
        final ActionFactory actionFactory = mockery.mock( ActionFactory.class );
        final Location l = new Location( "", "", null, actionFactory );
        mockery.checking( new Expectations() {{
            never( actionFactory ).createTakeAnItemAction(
                with( any( List.class ) ),
                with( any( UserInventory.class ) ),
                with( any( ModelLocation.class ) ) );
            ignoring( actionFactory );
        }});
        l.actions();
    }

    @Test
    public void location_coordinates_defaults_to_0_0() {
        Location l = createLocation();
        assertEquals( 0, l.x() );
        assertEquals( 0, l.y() );
    }

    @Test
    public void location_coordinates_can_be_set() {
        Location l = createLocation();
        l.setX( 5 );
        l.setY( 10 );
        assertEquals( 5,  l.x() );
        assertEquals( 10, l.y() );
    }
}

