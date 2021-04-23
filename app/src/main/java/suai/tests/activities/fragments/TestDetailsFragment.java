package suai.tests.activities.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import suai.tests.R;

public class TestDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";
    private static final String ID_TEST = "idTest";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private int idTest;

    public TestDetailsFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestDetailsFragment newInstance(int idTest) {
        TestDetailsFragment fragment = new TestDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ID_TEST, idTest);
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);

            idTest = getArguments().getInt(ID_TEST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_details, container, false);

        TextView id = root.findViewById(R.id.idTestTextView);
        id.setText(String.valueOf(idTest));

        return root;
    }
}