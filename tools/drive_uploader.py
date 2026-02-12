import warnings
warnings.filterwarnings("ignore")

import sys
import os
import json

from google.oauth2 import service_account
from googleapiclient.discovery import build
from googleapiclient.http import MediaFileUpload

SERVICE_ACCOUNT_FILE = "/secrets/service-account.json"
FOLDER_ID = os.environ.get("GOOGLE_DRIVE_FOLDER_ID")

def fail(msg):
    print(json.dumps({"error": msg}))
    sys.exit(1)

if not FOLDER_ID:
    fail("GOOGLE_DRIVE_FOLDER_ID env var not set")

if len(sys.argv) < 2:
    fail("Missing audio file path")

file_path = sys.argv[1]
if not os.path.exists(file_path):
    fail("File not found")

creds = service_account.Credentials.from_service_account_file(
    SERVICE_ACCOUNT_FILE,
    scopes=["https://www.googleapis.com/auth/drive"]
)

drive = build("drive", "v3", credentials=creds)

media = MediaFileUpload(file_path, mimetype="audio/mpeg", resumable=True)

file_metadata = {
    "name": os.path.basename(file_path),
    "parents": [FOLDER_ID]
}

uploaded = drive.files().create(
    body=file_metadata,
    media_body=media,
    fields="id, webViewLink, webContentLink"
).execute()

print(json.dumps({
    "fileId": uploaded["id"],
    "webViewLink": uploaded["webViewLink"],
    "webContentLink": uploaded["webContentLink"]
}))
