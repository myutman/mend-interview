### Input data format

```(json)
{
    "n_nodes": integer,
    "edges": [
        {
            "v1": integer,
            "v2": integer,
            "color": "red"/"blue"
        }, ...
    ],
    "start": integer
}
```

### Output data format

#### If there's an error
```(json)
{
    "status": "ERROR",
    "cause": "%What happened%"
}
```

#### If everything is correct
```(json)
{
    "status": "OK",
    "answer": [
        integer,
        ... // n_nodes times
    ]
}
```