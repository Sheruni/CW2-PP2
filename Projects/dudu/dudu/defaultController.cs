using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace dudu
{
    public class defaultController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}