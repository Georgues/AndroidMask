package com.example.foxchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Display;

import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Frame;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.AugmentedFaceNode;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private ModelRenderable foxRenderable;
    private boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CustomArFragment customArFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        ModelRenderable.builder()
                .setSource(this,R.raw.fox_face)
                .build()
                .thenAccept(renderable -> {
                    foxRenderable = renderable;

                    foxRenderable.setShadowCaster(false);
                    foxRenderable.setShadowReceiver(false);
                });

        customArFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customArFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if (foxRenderable == null)
                return;

            Frame frame = customArFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace: augmentedFaces){
                if (isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customArFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(foxRenderable);

                isAdded = true;
            }

        });


    }
}
