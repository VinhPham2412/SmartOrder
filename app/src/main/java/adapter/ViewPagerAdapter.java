package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import Fragments.CommunicationFragment;
import Fragments.BillFragment;
import Fragments.OrderFragment;
import Fragments.WaiterOrderFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private FragmentActivity fragmentActivity;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.fragmentActivity =fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new OrderFragment(fragmentActivity);
            case 1:
                return new BillFragment(fragmentActivity);
            case 2:
                return new WaiterOrderFragment(fragmentActivity);
            default:
                return new CommunicationFragment(fragmentActivity);

        }
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}
