 package com.hello_world.ronak.tradify;

 import android.app.IntentService;
 import android.content.Context;
 import android.content.Intent;

//import com.batch.android.Batch;

 /**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PushService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.hello_world.ronak.tradify.action.FOO";
    private static final String ACTION_BAZ = "com.hello_world.ronak.tradify.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.hello_world.ronak.tradify.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.hello_world.ronak.tradify.extra.PARAM2";

    public PushService() {
        super("PushService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, PushService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, PushService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*if (intent != null) {
            try
            {
                if( Batch.Push.shouldDisplayPush(this, intent) ) // Check that the push is valid
                {
                    String alert = intent.getStringExtra(Batch.Push.ALERT_KEY);
                    // Use the alert text to display the notification

                    //String title = intent.getStringExtra(Batch.Push.TITLE_KEY);
                    if( alert != null && !alert.trim().isEmpty() )
                    {
                        Toast.makeText(getApplicationContext(),"You have a message: "+alert,Toast.LENGTH_SHORT).show();
                    }
                }
            }
            finally
            {
                PushReceiver.completeWakefulIntent(intent);
            }
        }*/
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
