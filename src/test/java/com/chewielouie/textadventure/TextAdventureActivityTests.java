package com.chewielouie.textadventure;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import android.view.MotionEvent;
import android.widget.TextView;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.Robolectric;
import org.jmock.*;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class TextAdventureActivityTests {

    private Mockery mockery = new Mockery();

    private MotionEvent createUpMotionEvent( float x, float y ) {
        long downTime = 0;
        long eventTime = 0;
        int action = MotionEvent.ACTION_UP;
        int metaState = 0;
        return MotionEvent.obtain( downTime, eventTime, action, x, y, metaState );
    }

    private MotionEvent createDownMotionEvent( float x, float y ) {
        long downTime = 0;
        long eventTime = 0;
        int action = MotionEvent.ACTION_DOWN;
        int metaState = 0;
        return MotionEvent.obtain( downTime, eventTime, action, x, y, metaState );
    }

    @Test
    public void renders_the_presenter_on_resume() {
        final RendersView r = mockery.mock( RendersView.class );
        mockery.checking( new Expectations() {{
            oneOf( r ).render();
        }});

        new TextAdventureActivity( r ) {
            // Override to make onResume() accessible for testing
            @Override
            public void onResume() {
                super.onResume();
            }
        }.onResume();

        mockery.assertIsSatisfied();
    }

    @Test
    public void show_room_text_updates_text_view() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        activity.showLocationDescription( "cheese" );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        assertEquals( "cheese", t.getText().toString() );
    }

    @Test
    public void down_touch_event_is_ignored() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        mockery.checking( new Expectations() {{
            never( handler ).moveThroughExit( with( any( Exit.class ) ) );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        activity.dispatchTouchEvent( createDownMotionEvent( 0, 0 ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_top_right_quadrant_causes_first_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "first exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 75;
        float y = 0;
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_top_left_quadrant_causes_first_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "first exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( exit );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 25;
        float y = 0;
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_bottom_right_quadrant_causes_second_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "second exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 75;
        float y = 99;
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_bottom_left_quadrant_causes_second_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "second exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 25;
        float y = 99;
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_right_bottom_quadrant_causes_third_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "third exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 99;
        float y = 75;
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_right_top_quadrant_causes_third_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "third exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 99;
        float y = 25;
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_left_bottom_quadrant_causes_fourth_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "fourth exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( new Exit( "third exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 1;
        float y = 75;
        assertTrue( 1 < (t.getLayoutParams().height-75) );
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_left_top_quadrant_causes_fourth_exit_to_be_used() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        final Exit exit = new Exit( "fourth exit" );
        mockery.checking( new Expectations() {{
            oneOf( handler ).moveThroughExit( exit );
            ignoring( handler );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( new Exit( "third exit" ) );
        exits.add( exit );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.main_text_output );
        t.getLayoutParams().width = 100;
        t.getLayoutParams().height = 100;
        float x = 1;
        float y = 25;
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    @Test
    public void touch_event_in_top_quadrant_with_no_exits_is_ignored() {
        final UserActionHandler handler = mockery.mock( UserActionHandler.class );
        mockery.checking( new Expectations() {{
            never( handler ).moveThroughExit( with( any( Exit.class ) ) );
        }});
        TextAdventureActivity activity = new TextAdventureActivity( handler );

        float x = 0;
        float y = 0;
        activity.dispatchTouchEvent( createUpMotionEvent( x, y ) );

        mockery.assertIsSatisfied();
    }

    //@Test
    //public void touch_event_in_second_quadrant_with_only_one_exit_is_ignored() {
    //@Test
    //public void touch_event_in_third_quadrant_with_only_two_exits_is_ignored() {
    //@Test
    //public void touch_event_in_fourth_quadrant_with_only_three_exits_is_ignored() {

    @Test
    public void top_direction_label_uses_first_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void bottom_direction_label_uses_second_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "second exit", t.getText().toString() );
    }

    @Test
    public void right_direction_label_uses_third_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( new Exit( "third exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "third exit", t.getText().toString() );
    }

    @Test
    public void left_direction_label_uses_fourth_exit_text() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit" ) );
        exits.add( new Exit( "third exit" ) );
        exits.add( new Exit( "fourth exit" ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "fourth exit", t.getText().toString() );
    }

    @Test
    public void top_direction_label_is_blank_if_no_exits() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "", t.getText().toString() );
    }

    @Test
    public void bottom_direction_label_is_blank_if_less_than_two_exits() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "", t.getText().toString() );
    }

    @Test
    public void right_direction_label_is_blank_if_less_than_three_exits() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "", t.getText().toString() );
    }

    @Test
    public void left_direction_label_is_blank_if_less_than_four_exits() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "", t.getText().toString() );
    }

    @Test
    public void exit_with_north_direction_hint_is_preferred_on_top_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit", "dest", Exit.DirectionHint.North ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "second exit", t.getText().toString() );
    }

    @Test
    public void exit_with_south_direction_hint_is_preferred_on_bottom_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit", "dest", Exit.DirectionHint.South ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exit_with_east_direction_hint_is_preferred_on_right_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit", "dest", Exit.DirectionHint.East ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exit_with_west_direction_hint_is_preferred_on_left_label() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit", "dest", Exit.DirectionHint.West ) );
        activity.showLocationExits( exits );

        final TextView t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "first exit", t.getText().toString() );
    }

    @Test
    public void exits_without_direction_hints_fill_the_holes() {
        TextAdventureActivity activity = new TextAdventureActivity();
        activity.onCreate( null );

        List<Exit> exits = new ArrayList<Exit>();
        exits.add( new Exit( "first exit" ) );
        exits.add( new Exit( "second exit", "dest", Exit.DirectionHint.North ) );
        exits.add( new Exit( "third exit" ) );
        exits.add( new Exit( "fourth exit", "dest", Exit.DirectionHint.East  ) );
        activity.showLocationExits( exits );

        TextView t = (TextView)activity.findViewById( R.id.top_direction_label );
        assertEquals( "second exit", t.getText().toString() );
        t = (TextView)activity.findViewById( R.id.bottom_direction_label );
        assertEquals( "first exit", t.getText().toString() );
        t = (TextView)activity.findViewById( R.id.right_direction_label );
        assertEquals( "fourth exit", t.getText().toString() );
        t = (TextView)activity.findViewById( R.id.left_direction_label );
        assertEquals( "third exit", t.getText().toString() );
    }
}

