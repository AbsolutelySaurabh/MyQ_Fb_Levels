package dd.com.myq.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import dd.com.myq.Activity.FindFriendsActivity;
import dd.com.myq.Fragment.Friends.Friend;
import dd.com.myq.Fragment.Friends.FriendAdapter;
import dd.com.myq.R;
import dd.com.myq.Util.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int FRIEND_LOADER_ID = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View layout;

    View No_friends, click_find_friends;

    ListAdapter name_arrayAdapter;
    ListAdapter points_arrayAdapter;

    View rootView;
    public int flag=0;

    //   private FriendAdapter friendAdapter;

//    public ListView listView;

    private ArrayList<String> al_name = new ArrayList<String>();
    private ArrayList<String> al_points = new ArrayList<String>();


    ArrayList<Friend> friends;
    ListView listView;
    private FriendAdapter friendAdapter;


    //    SessionManager currentSession;
    public String REQUEST_GET_FRIENDS = "http://myish.com:10011/api/getfriends/";

    private OnFragmentInteractionListener mListener;

    public FriendFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        currentSession = new SessionManager(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_friend, container, false);

        SessionManager currentSession = new SessionManager(getActivity());

        HashMap<String, String> user_details = currentSession.getUserDetails();
        String user_id = user_details.get(SessionManager.KEY_UID);
        REQUEST_GET_FRIENDS = REQUEST_GET_FRIENDS + user_id;

        Log.d("GET FRIENDS FRAGMENT ", REQUEST_GET_FRIENDS);

        listView=(ListView)rootView.findViewById(R.id.list);

        layout =  rootView.findViewById(R.id.added_friends_progress_bar);
        layout.setVisibility(View.VISIBLE);

//        No_friends = rootView.findViewById(R.id.no_friends);
//        click_find_friends = rootView.findViewById(R.id.click_find_friends)


        friends= new ArrayList<>();

        if(flag==0) {

            getAddedFriends(REQUEST_GET_FRIENDS);
        }

        TextView findFriends = (TextView) rootView.findViewById(R.id.add_friends_text);
        findFriends.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FindFriendsActivity.class);
                startActivity(intent);

            }
        });
        return rootView;
    }

    public void getAddedFriends(final String REQUEST_GET_FRIENDS_URL){

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(getActivity(), REQUEST_GET_FRIENDS_URL , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray responseArray) {

                if(flag==1){

                    friendAdapter.clear();
                    al_name.clear();
                    al_points.clear();
                }

                // Log.e("Response Shushant GET: ", response.toString());

                try {

                    JSONObject responseObject = responseArray.getJSONObject(0);
                    JSONArray friendsarray = responseObject.getJSONArray("friends");

                    for (int i = 0; i < friendsarray.length(); i++) {

                        JSONObject object = friendsarray.getJSONObject(i);

                        //  Log.e("Fragment getFriends ", String.valueOf(object));

                        String name = object.getString("friendname");
                        String points = object.getString("friendpoints");

                        al_points.add(points);
                        al_name.add(name);

                    }

                    layout.setVisibility(View.GONE);

                    Log.d("al_name", String.valueOf(al_name.size()));
                    Log.d("al_points", String.valueOf(al_points.size()));

                    for (int i = 0; i < al_name.size(); i++) {

                        friends.add(new Friend(al_name.get(i), al_points.get(i)));

                    }

//                friends.add(new Friend("Saurabh","100"));
//                friends.add(new Friend("Rishabh","100"));
//                friends.add(new Friend("Amitosh","100"));
//                friends.add(new Friend("Ajayant","100"));

                    friendAdapter = new FriendAdapter(friends, getContext());

                    listView.setAdapter(friendAdapter);

                    flag=1;


                } catch (JSONException e) {

                    Log.e("QueryUtils", "Problem parsing the friends JSON results", e);
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                layout.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Error Loading Friends", Toast.LENGTH_SHORT).show();


            }
        });

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getFragmentManager().findFragmentByTag("MyFragment") != null) {
            getFragmentManager().findFragmentByTag("MyFragment").setRetainInstance(true);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        friendAdapter.notifyDataSetChanged();
        if (flag==1) {
            // getFragmentManager().findFragmentByTag("MyFragment").setRetainInstance(true);

            friendAdapter.clear();
            al_name.clear();
            al_points.clear();

            Log.d("ON RESUME() ", "kkkkkkkkkk");
            getAddedFriends(REQUEST_GET_FRIENDS);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
