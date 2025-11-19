package com.example.mymvvmnewsapp

import android.app.Application

/**
 * Minimal Application subclass used to access application-level context (e.g., for
 * connectivity checks) from within the [NewsViewModel].
 */
class NewsApplication : Application()