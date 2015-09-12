package com.cesarandres.ps2link.module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.cesarandres.ps2link.R;
import com.cesarandres.ps2link.dbg.DBGCensus;
import com.cesarandres.ps2link.dbg.DBGCensus.Namespace;

/**
 * @author Cesar Ramirez
 *         <p/>
 *         This class will be in charge of switching between the three available buttons to select
 *         from the ps2:v1, ps2ps4us:v2 and ps2ps4eu:v2 namespace
 */
@SuppressLint("CutPasteId")
public class ButtonSelectSource {

    private ImageButton pcNamespaceButton;
    private ImageButton ps4euNamespaceButton;
    private ImageButton ps4usNamespaceButton;
    private Context context;

    private SourceSelectionChangedListener listener;

    public ButtonSelectSource(Context context, ViewGroup root) {
        this.context = context;
        SharedPreferences settings = context.getSharedPreferences("PREFERENCES", 0);
        String lastNamespace = settings.getString("lastNamespace", Namespace.PS2PC.name());
        Namespace currentNamespace = Namespace.valueOf(lastNamespace);
        DBGCensus.currentNamespace = currentNamespace;

        pcNamespaceButton = (ImageButton) View.inflate(context, R.layout.layout_title_button, null);
        pcNamespaceButton.setImageResource(R.drawable.namespace_pc);
        pcNamespaceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonState(Namespace.PS2PS4US);
            }
        });

        ps4euNamespaceButton = (ImageButton) View.inflate(context, R.layout.layout_title_button, null);
        ps4euNamespaceButton.setImageResource(R.drawable.namespace_ps4eu);
        ps4euNamespaceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonState(Namespace.PS2PC);
            }
        });

        ps4usNamespaceButton = (ImageButton) View.inflate(context, R.layout.layout_title_button, null);
        ps4usNamespaceButton.setImageResource(R.drawable.namespace_ps4us);
        ps4usNamespaceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateButtonState(Namespace.PS2PS4EU);
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                3,
                r.getDisplayMetrics()
        );

        params.setMargins(px, px, px, px);

        root.addView(pcNamespaceButton, params);
        root.addView(ps4euNamespaceButton, params);
        root.addView(ps4usNamespaceButton, params);
        updateButtonState(currentNamespace);
    }

    private void updateButtonState(Namespace namespace) {
        SharedPreferences settings = this.context.getSharedPreferences("PREFERENCES", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lastNamespace", namespace.name());
        editor.commit();
        DBGCensus.currentNamespace = namespace;
        pcNamespaceButton.setVisibility(namespace == Namespace.PS2PC ? View.VISIBLE : View.GONE);
        ps4euNamespaceButton.setVisibility(namespace == Namespace.PS2PS4EU ? View.VISIBLE : View.GONE);
        ps4usNamespaceButton.setVisibility(namespace == Namespace.PS2PS4US ? View.VISIBLE : View.GONE);

        if (listener != null) {
            listener.onSourceSelectionChanged(namespace);
        }
    }

    public void removeButtons(Context context, ViewGroup root) {
        root.removeView(pcNamespaceButton);
        root.removeView(ps4euNamespaceButton);
        root.removeView(ps4usNamespaceButton);
    }

    public SourceSelectionChangedListener getListener() {
        return listener;
    }

    public void setListener(SourceSelectionChangedListener listener) {
        this.listener = listener;
    }

    public interface SourceSelectionChangedListener {
        public void onSourceSelectionChanged(DBGCensus.Namespace selectedNamespace);
    }
}
