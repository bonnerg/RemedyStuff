Get-ChildItem -path "C:\Program Files\BMC Software\ARSystem\midtier\attstore" | where {$_.LastWritetime -lt(date).addhours(-1)} | remove-item