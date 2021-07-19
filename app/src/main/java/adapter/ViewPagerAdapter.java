package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.CommunicationFragment;
import Fragments.GetFoodFragment;
import Fragments.BillFragment;
import Fragments.OrderFragment;
import Fragments.WaiterOrderFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new OrderFragment();
            case 1:
                return new BillFragment();
            case 2:
                return new WaiterOrderFragment();
            default:
                return new CommunicationFragment();

        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}
