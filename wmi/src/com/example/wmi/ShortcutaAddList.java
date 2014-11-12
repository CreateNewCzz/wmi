package com.example.wmi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShortcutaAddList extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getAction().equals(Intent.ACTION_CREATE_SHORTCUT)) {

			Intent returnItent = new Intent();

			returnItent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "wmi");
			returnItent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
					Intent.ShortcutIconResource.fromContext(this,
							R.drawable.ic_launcher));

			returnItent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this,
					MainActivity.class));

			setResult(RESULT_OK, returnItent);
			finish();

		}

	}

}
