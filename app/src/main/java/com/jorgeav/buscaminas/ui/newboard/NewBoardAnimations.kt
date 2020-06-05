/*
 * Copyright 2020 Jorge Ávila Estévez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jorgeav.buscaminas.ui.newboard

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

/**
 * Created by Jorge Avila on 05/06/2020.
 */
object NewBoardAnimations {
    fun animationRightOut(view: View, listener: Animator.AnimatorListener? = null): Animator {
        // Move right from position and fade out
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 75f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val moveAnimator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            translationX,
            alpha
        )
        moveAnimator.duration = 50

        // End in original position but faded out
        val undoTranslationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f)
        val undoMoveAnimator = ObjectAnimator.ofPropertyValuesHolder(view, undoTranslationX)
        undoMoveAnimator.duration = 0

        return AnimatorSet().apply {
            play(moveAnimator).before(undoMoveAnimator)
            if (listener != null) addListener(listener)
        }
    }

    fun animationRightIn(view: View, listener: Animator.AnimatorListener? = null): Animator {
        // Start in left from position and faded out
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -75f)
        val moveAnimator = ObjectAnimator.ofPropertyValuesHolder(view, translationX)
        moveAnimator.duration = 0

        // Move left from started position and fade in
        val undoTranslationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f)
        val undoMoveAnimator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            undoTranslationX,
            alpha
        )
        undoMoveAnimator.duration = 50

        return AnimatorSet().apply {
            play(moveAnimator).before(undoMoveAnimator)
            if (listener != null) addListener(listener)
        }
    }

    fun animationLeftOut(view: View, listener: Animator.AnimatorListener? = null): Animator {
        // Move left from position and fade out
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -75f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val moveAnimator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            translationX,
            alpha
        )
        moveAnimator.duration = 50

        // End in original position but faded out
        val undoTranslationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f)
        val undoMoveAnimator = ObjectAnimator.ofPropertyValuesHolder(view, undoTranslationX)
        undoMoveAnimator.duration = 0

        return AnimatorSet().apply {
            play(moveAnimator).before(undoMoveAnimator)
            if (listener != null) addListener(listener)
        }
    }

    fun animationLeftIn(view: View, listener: Animator.AnimatorListener? = null): Animator {
        // Start in right from position and faded out
        val translationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 75f)
        val moveAnimator = ObjectAnimator.ofPropertyValuesHolder(view, translationX)
        moveAnimator.duration = 0

        // Move left from started position and fade in
        val undoTranslationX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f)
        val undoMoveAnimator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            undoTranslationX,
            alpha
        )
        undoMoveAnimator.duration = 50

        return AnimatorSet().apply {
            play(moveAnimator).before(undoMoveAnimator)
            if (listener != null) addListener(listener)
        }
    }
}