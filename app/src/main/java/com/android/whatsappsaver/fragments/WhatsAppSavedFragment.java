package com.android.whatsappsaver.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.whatsappsaver.R;
import com.android.whatsappsaver.StatusModel;
import com.android.whatsappsaver.Utils;
import com.android.whatsappsaver.adapter.SavedStatusAdapter;

import java.io.File;
import java.util.ArrayList;

public class WhatsAppSavedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerView;
    TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<StatusModel> arrayList;
    public WhatsAppSavedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whats_app_saved, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        textView = view.findViewById(R.id.emptyText);
        swipeRefreshLayout = view.findViewById(R.id.contentView);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);

        loadData();

        return view;
    }

    private void loadData(){
        arrayList = new ArrayList<>();
        String path = Utils.statusSaverPath;
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            String[] paths = {""};
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith("gif") || files[i].getName().endsWith(".mp4")) {
                            paths[0] = path + "" + files[i].getName();
                            StatusModel statusModel = new StatusModel(paths[0],
                                    files[i].getName().substring(0, files[i].getName().length() - 4),
                                    0);
                            arrayList.add(statusModel);
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void unused) {
                    super.onPostExecute(unused);
                    if (!(arrayList.toArray().length > 0)) {
                        textView.setText("You Have Not Saved Any Status Yet");
                        textView.setVisibility(View.VISIBLE);
                    }
                    SavedStatusAdapter adapter = new SavedStatusAdapter(requireContext(), arrayList);
                    recyclerView.setAdapter(adapter);

                    LinearLayoutManager linearLayoutManager = new GridLayoutManager(requireContext(), 2);
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
            }.execute();
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText("No Status Available");
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        loadData();
    }
}