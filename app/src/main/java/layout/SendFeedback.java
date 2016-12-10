package layout;

import android.accounts.AccountManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.florim.thirremjeshtrin.Authenticator;
import com.example.florim.thirremjeshtrin.ConnectToServer;
import com.example.florim.thirremjeshtrin.PermissionUtils;
import com.example.florim.thirremjeshtrin.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendFeedback.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendFeedback#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendFeedback extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText feedback;
    Button btnSendFeedback;
    EditText rating;
    TextView info;
    RatingBar rb;

    String RepairmanID;
    ConnectToServer connectToServer;
    Map<String ,String> accountData;
    AccountManager am;
    float ratingValue;

    public SendFeedback() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendFeedback.
     */
    // TODO: Rename and change types and number of parameters
    public static SendFeedback newInstance(String param1, String param2) {
        SendFeedback fragment = new SendFeedback();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_feedback, container, false);

        am = AccountManager.get(getActivity());
        accountData = Authenticator.findAccount(am, getActivity());

        feedback = (EditText) getView().findViewById(R.id.txtFeedback);
        btnSendFeedback = (Button)  view.findViewById(R.id.btnFeedback);
        info = (TextView) view.findViewById(R.id.txtInfo);
        rb = (RatingBar) view.findViewById(R.id.ratingBar);

        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValue = v;
            }
        });

        info.setVisibility(View.GONE);
        connectToServer = new ConnectToServer();

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            RepairmanID = bundle.getString("RepairmanID");
        }
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {

            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean connectivity = PermissionUtils.connectivityCheck(cm);

            @Override
            public void onClick(View view) {

                String feed = feedback.getText().toString();
                if(!feed.isEmpty()) {
                    Map<String, String> params = new HashMap<>();
                    params.put("UserID", accountData.get("UserID"));
                    params.put("RepID", RepairmanID);
                    params.put("Feedback", feed);
                    params.put("Rating", Float.toString(ratingValue));

                    if (connectivity) {
                        connectToServer = new ConnectToServer();
                        connectToServer.sendRequest(connectToServer.FEEDBACK, params, true);
                        List<Map<String, String>> response = connectToServer.results;
                        if (!response.isEmpty()) {
                            Map<String, String> result = new HashMap<String, String>();
                            result = response.get(0);
                            if (result.get("message") == "INSERTED") {
                                Toast.makeText(getActivity().getApplicationContext(), "SENT!", Toast.LENGTH_LONG).show();
                            } else {
                            }
                        } else {

                        }
                    }
                }
                else{
                    if(feed.isEmpty()){
                        feedback.setError("");
                    }
                }
            }
        });
        return view;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
