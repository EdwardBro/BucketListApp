package com.example.bucketlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, DreamAdapter.CheckButtonListener {

	private RecyclerView rvDreamList;
	private DreamAdapter dreamAdapter;
	private List<Dream> dreamList = new ArrayList<>();
	private DreamRoomDatabase db;
	public static final int REQUEST_CODE = 1234;
	public static final String EXTRATEXT_TITLE = "title";
	public static final String EXTRATEXT_DESCR = "descr";

	private GestureDetector gestureDetector;
	private Executor executor = Executors.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		db = DreamRoomDatabase.getDatabase(this);
		toolbar.setNavigationIcon(R.drawable.ic_delete_white_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteAllDreams(dreamList);
			}
		});
		//DELETE BUTTON IS NOT COMPLETE!
		initRecyclerView();
		initFloatingActionButton();

	}


	private void initFloatingActionButton() {
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, AddActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});
	}

	private void initRecyclerView(){
		dreamAdapter = new DreamAdapter(dreamList, this);
		rvDreamList = findViewById(R.id.recyclerView);
		rvDreamList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
		rvDreamList.setAdapter(dreamAdapter);
		rvDreamList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public void onLongPress(MotionEvent e){
				super.onLongPress(e);
				View child = rvDreamList.findChildViewUnder(e.getX(), e.getY());
				if(child != null){
					int adapterPosition = rvDreamList.getChildAdapterPosition(child);
					deleteDream(dreamList.get(adapterPosition));
				}
			}
		});
		rvDreamList.addOnItemTouchListener(this);
		getAllDreams();
	}

	private void updateUI(List<Dream> dreams) {
		dreamList.clear();
		dreamList.addAll(dreams);
		dreamAdapter.notifyDataSetChanged();
	}

	private void getAllDreams() {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				final List<Dream> dreams = db.dreamDao().getallDreams();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateUI(dreams);
					}
				});
			}
		});
	}

	private void insertDream(final Dream dream) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.dreamDao().insertDream(dream);
			}
		});
	}

	private void deleteDream(final Dream dream) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.dreamDao().deleteDream(dream);
				getAllDreams();
			}
		});
	}

	private void updateDream(final Dream dream) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.dreamDao().updateDream(dream);
				getAllDreams();
			}
		});
	}

	private void deleteAllDreams(final List<Dream> dreamList) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				db.dreamDao().deleteDream((Dream) dreamList);
				getAllDreams();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_delete_item) {
			deleteAllDreams(dreamList);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
		gestureDetector.onTouchEvent(motionEvent);
		return false;
	}

	@Override
	public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(REQUEST_CODE == requestCode){
			if(resultCode == RESULT_OK) {
				String title = data.getStringExtra(EXTRATEXT_TITLE);
				String descr = data.getStringExtra(EXTRATEXT_DESCR);
				Dream dream = new Dream(title, descr);
				insertDream(dream);
				getAllDreams();
			}
		}
	}

	@Override
	public void onCheckClick(Dream dream) {
		updateDream(dream);
		getAllDreams();
	}
}
