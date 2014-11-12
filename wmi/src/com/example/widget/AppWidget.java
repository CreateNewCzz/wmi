package com.example.widget;

import com.example.wmi.MainActivity;
import com.example.wmi.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class AppWidget extends AppWidgetProvider {

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.layout_appwidget);

		views.setOnClickPendingIntent(R.id.widget_img, PendingIntent
				.getActivity(context, 0,
						new Intent(context, MainActivity.class), 0));

		appWidgetManager.updateAppWidget(appWidgetIds, views);

	}

	//Ĭ����Ϣ���� ����
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	// ɾ��widget
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	// ��һ��  addwidget
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	// ���һ��appwidget ɾ��
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

}
