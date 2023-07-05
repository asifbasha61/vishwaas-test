from rest_framework import routers
from django.urls import path
from .views import Certificate,Verification


router = routers.DefaultRouter()
urlpatterns = [
    path('certificates', Certificate.as_view()),
    path('verification/<str:certificate_id>', Verification.as_view()),
]
