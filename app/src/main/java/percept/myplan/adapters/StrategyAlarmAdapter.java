package percept.myplan.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import percept.myplan.POJO.Alarm;
import percept.myplan.R;

/**
 * Created by percept on 16/7/16.
 */

public class StrategyAlarmAdapter extends RecyclerView.Adapter<StrategyAlarmAdapter.SymptomHolder> {


    public List<Alarm> LIST_SYMPTOMSTRATEGY;

    public class SymptomHolder extends RecyclerView.ViewHolder {
        public TextView TV_STRATEGYALARMNAME;
        public TextView TV_STRATEGYALARMTIME;

        public SymptomHolder(View itemView) {
            super(itemView);
            TV_STRATEGYALARMNAME = (TextView) itemView.findViewById(R.id.tvAlarmNameStrategy);
            TV_STRATEGYALARMTIME= (TextView) itemView.findViewById(R.id.tvAlarmTimeStrategy);
        }
    }

    public StrategyAlarmAdapter(List<Alarm> quickMessageList) {
        this.LIST_SYMPTOMSTRATEGY = quickMessageList;
    }

    @Override
    public SymptomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strategy_alarm_list_item, parent, false);

        return new SymptomHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SymptomHolder holder, int position) {
        Alarm _symptom = LIST_SYMPTOMSTRATEGY.get(position);
        String hm = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toHours(Long.parseLong(_symptom.getAlarmTime())),
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(_symptom.getAlarmTime())) % TimeUnit.HOURS.toMinutes(1));

        holder.TV_STRATEGYALARMNAME.setText(_symptom.getAlarmName());
        holder.TV_STRATEGYALARMTIME.setText(hm);
    }

    @Override
    public int getItemCount() {
        return this.LIST_SYMPTOMSTRATEGY.size();
    }
}
