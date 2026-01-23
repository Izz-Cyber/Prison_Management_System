Prison Management System - تشغيل المشروع

ملخص سريع:
- المشروع يدعم وضعين: متصل بقاعدة MySQL الحقيقي أو وضع العرض (demo) باستخدام بيانات Mock.
- إن لم يكن لديك MySQL Connector/J في مجلد `lib/` سيعمل التطبيق تلقائياً في وضع العرض.

الخطوات لتشغيل (Windows):

1) تجميع المشروع (مرة واحدة أو بعد أي تعديل):
   افتح PowerShell في مجلد المشروع ثم:
```powershell
Set-Location 'C:\Users\PC\Desktop\PrisonManagementSystem_Updated\PrisonManagementSystem\src'
Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName } | Out-File sources.txt -Encoding ASCII
cmd /c "javac -d ..\out @sources.txt"
```

2) تشغيل باستخدام MySQL (إذا أضفت الدرايفر):
   - ضع `mysql-connector-java-8.0.xx.jar` داخل مجلد `lib\` بالمشروع.
   - شغّل باستخدام السكريبت `run.bat` أو `run.ps1`:
```powershell
# من المجلد الجذري للمشروع
.\run.bat
# أو
.\run.ps1
```

3) تشغيل في وضع العرض (demo):
   - إن لم تضع الدرايفر، السكريبت سيشغّل التطبيق بدون JAR ويعرض بيانات تجريبية.

نصائح:
- تأكد أن خدمة MySQL تعمل إذا أردت الاتصال الحقيقي.
- عدّل بيانات الاتصال (المستخدم/كلمة المرور) في `src/DB/DBConnection.java` حسب إعداداتك.

موجود أيضاً:
- `src/DB/MockDBManager.java` — بيانات تجريبية في الذاكرة.
- `src/DB/DBManagerInterface.java` — واجهة تسمح بالتبديل بين Mock وDB الحقيقي.

إذا تريد أضيف سكريبت يقوم بتحميل الـ JAR تلقائياً من الإنترنت أخبرني وسأعده لك (قد يتطلب صلاحيات وتحميل خارجي).
