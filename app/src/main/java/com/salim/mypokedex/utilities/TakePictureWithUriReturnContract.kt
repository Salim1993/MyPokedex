package com.salim.mypokedex.utilities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper

class TakePictureWithUriReturnContract : ActivityResultContract<Uri, TakePictureWithUriReturnContract.Result>() {

    private lateinit var imageUri: Uri

    @CallSuper
    override fun createIntent(context: Context, input: Uri): Intent {
        imageUri = input
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun getSynchronousResult(
        context: Context,
        input: Uri
    ): SynchronousResult<Result>? = null

    @Suppress("AutoBoxing")
    override fun parseResult(resultCode: Int, intent: Intent?): Result {
        return Result((resultCode == Activity.RESULT_OK), imageUri)
    }

    inner class Result(val isSuccess: Boolean, val result: Uri)
}