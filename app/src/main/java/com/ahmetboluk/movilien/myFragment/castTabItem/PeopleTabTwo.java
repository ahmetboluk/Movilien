package com.ahmetboluk.movilien.myFragment.castTabItem;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmetboluk.movilien.data.peopleDetail.PersonDetail;
import com.ahmetboluk.movilien.myFragment.DetailFragment;
import com.ahmetboluk.movilien.R;
import com.ahmetboluk.movilien.RecyclerItemClickListener;
import com.ahmetboluk.movilien.adapter.PersonDetailAdapter;

public class PeopleTabTwo extends Fragment {

    PersonDetail personDetail;
    private RecyclerView recyclerView;
    private PersonDetailAdapter personDetailAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private int birkere=0;
    private int SELECTED=0;
    private int SELECTED_MOVIE=0;
    private int SELECTED_TV=1;

    private long mLastClickTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_people_tab_two, container, false);

        recyclerView = view.findViewById(R.id.rv_person_movie);
        personDetail = (PersonDetail) getArguments().getSerializable("personDetail");

        SELECTED=SELECTED_MOVIE;

        personDetailAdapter= new PersonDetailAdapter(getContext(),personDetail);
        layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new PeopleTabTwo.GridSpacingItemDecoration(9, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(personDetailAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                            return;
                        }
                        mLastClickTime = SystemClock.elapsedRealtime();
                        DetailFragment detailFragment = new DetailFragment();
                        Bundle data=new Bundle();
                        data.putInt("movie_id", personDetail.getMovieCredits().getCast().get(position).getId());
                        data.putInt("selected",SELECTED);
                        detailFragment.setArguments(data);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.main_activity,detailFragment,null);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );



        return view;
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));

    }

}